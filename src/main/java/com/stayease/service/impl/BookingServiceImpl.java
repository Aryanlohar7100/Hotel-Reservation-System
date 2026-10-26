package com.stayease.service.impl;

import com.stayease.dto.request.BookingRequest;
import com.stayease.dto.response.BookingResponse;
import com.stayease.entity.*;
import com.stayease.exception.InvalidBookingException;
import com.stayease.exception.ResourceNotFoundException;
import com.stayease.exception.RoomUnavailableException;
import com.stayease.repository.BookingRepository;
import com.stayease.repository.HotelRepository;
import com.stayease.repository.PaymentRepository;
import com.stayease.repository.RoomRepository;
import com.stayease.repository.UserRepository;
import com.stayease.service.BookingService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              RoomRepository roomRepository,
                              HotelRepository hotelRepository,
                              UserRepository userRepository,
                              PaymentRepository paymentRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public synchronized BookingResponse createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        LocalDate checkIn = request.getCheckInDate();
        LocalDate checkOut = request.getCheckOutDate();

        if (checkIn.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Check-in date cannot be in the past");
        }
        if (!checkOut.isAfter(checkIn)) {
            throw new InvalidBookingException("Check-out date must be at least one day after check-in date");
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + request.getRoomId()));

        if (!room.isActive()) {
            throw new RoomUnavailableException("This room is currently inactive or under maintenance");
        }

        Hotel hotel = room.getHotel();
        if (!hotel.isActive()) {
            throw new RoomUnavailableException("The selected hotel is currently inactive");
        }

        int roomCapacity = room.getCapacityAdults() + room.getCapacityChildren();
        if (request.getTotalGuests() > roomCapacity) {
            throw new InvalidBookingException("Total guests (" + request.getTotalGuests() +
                    ") exceeds maximum room capacity (" + roomCapacity + ")");
        }

        // Concurrency double-booking verification
        boolean available = roomRepository.isRoomAvailable(room.getId(), checkIn, checkOut);
        if (!available) {
            throw new RoomUnavailableException("Room " + room.getRoomNumber() +
                    " is no longer available for the selected dates. Please select another room or alternative dates.");
        }

        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        if (nights <= 0) {
            nights = 1;
        }

        BigDecimal pricePerNight = room.getBasePricePerNight();
        BigDecimal totalAmount = pricePerNight.multiply(BigDecimal.valueOf(nights));

        String bookingRef = generateBookingReference();

        Booking booking = new Booking();
        booking.setBookingReference(bookingRef);
        booking.setUser(user);
        booking.setHotel(hotel);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setTotalGuests(request.getTotalGuests());
        booking.setTotalAmount(totalAmount);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        BookingItem item = new BookingItem();
        item.setBooking(booking);
        item.setRoom(room);
        item.setPricePerNight(pricePerNight);
        item.setNightsCount((int) nights);
        item.setSubtotal(totalAmount);
        booking.setItems(Collections.singletonList(item));

        // Create Payment record
        String transactionId = "TXN-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTransactionId(transactionId);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        booking.setPayment(payment);

        Booking savedBooking = bookingRepository.save(booking);
        return BookingResponse.fromEntity(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingByReference(String reference, String userEmail) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference: " + reference));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_ADMIN && !booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this reservation");
        }

        return BookingResponse.fromEntity(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_ADMIN && !booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to view this reservation");
        }

        return BookingResponse.fromEntity(booking);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.ROLE_ADMIN && !booking.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have permission to cancel this reservation");
        }

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingException("This reservation has already been cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking.setUpdatedAt(LocalDateTime.now());

        if (booking.getPayment() != null) {
            booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
        }

        Booking updated = bookingRepository.save(booking);
        return BookingResponse.fromEntity(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getAllBookingsAdmin() {
        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponse updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        booking.setBookingStatus(newStatus);
        booking.setUpdatedAt(LocalDateTime.now());

        if (newStatus == BookingStatus.CANCELLED && booking.getPayment() != null) {
            booking.getPayment().setPaymentStatus(PaymentStatus.REFUNDED);
        }

        Booking saved = bookingRepository.save(booking);
        return BookingResponse.fromEntity(saved);
    }

    private String generateBookingReference() {
        int year = LocalDate.now().getYear();
        String randomSuffix = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "STY-" + year + "-" + randomSuffix;
    }
}
