package com.stayease.service;

import com.stayease.dto.request.BookingRequest;
import com.stayease.dto.response.BookingResponse;
import com.stayease.entity.*;
import com.stayease.exception.InvalidBookingException;
import com.stayease.exception.RoomUnavailableException;
import com.stayease.repository.*;
import com.stayease.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User sampleUser;
    private Hotel sampleHotel;
    private Room sampleRoom;

    @BeforeEach
    void setUp() {
        sampleUser = new User("guest@example.com", "hash", "Guest User", "1234567890", Role.ROLE_CUSTOMER);
        sampleUser.setId(1L);

        sampleHotel = new Hotel("The Grand Horizon", "Luxury hotel", "Marine Drive", "Mumbai", "Maharashtra", "India",
                "info@hotel.com", "12345", 4.8, "https://img.url");
        sampleHotel.setId(10L);

        sampleRoom = new Room(sampleHotel, "101", RoomType.DELUXE, new BigDecimal("5000.00"), 2, 1, "WiFi, AC", "https://img.url");
        sampleRoom.setId(101L);
    }

    @Test
    @DisplayName("Should create booking successfully with valid dates, price calculation and reference")
    void testCreateBooking_Success() {
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5); // 3 nights

        BookingRequest request = new BookingRequest();
        request.setHotelId(10L);
        request.setRoomId(101L);
        request.setCheckInDate(checkIn);
        request.setCheckOutDate(checkOut);
        request.setTotalGuests(2);
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        when(userRepository.findByEmail("guest@example.com")).thenReturn(Optional.of(sampleUser));
        when(roomRepository.findById(101L)).thenReturn(Optional.of(sampleRoom));
        when(roomRepository.isRoomAvailable(101L, checkIn, checkOut)).thenReturn(true);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> {
            Booking b = invocation.getArgument(0);
            b.setId(500L);
            return b;
        });

        BookingResponse response = bookingService.createBooking(request, "guest@example.com");

        assertNotNull(response);
        assertEquals(500L, response.getId());
        assertNotNull(response.getBookingReference());
        assertTrue(response.getBookingReference().startsWith("STY-"));
        assertEquals(3, response.getTotalNights());
        assertEquals(new BigDecimal("15000.00"), response.getTotalAmount()); // 3 nights * 5000
        assertEquals(BookingStatus.CONFIRMED, response.getBookingStatus());
        assertEquals("SUCCESS", response.getPaymentStatus());

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should throw RoomUnavailableException when room is already reserved for requested dates")
    void testCreateBooking_DoubleBookingConflict() {
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(5);

        BookingRequest request = new BookingRequest();
        request.setHotelId(10L);
        request.setRoomId(101L);
        request.setCheckInDate(checkIn);
        request.setCheckOutDate(checkOut);
        request.setTotalGuests(2);

        when(userRepository.findByEmail("guest@example.com")).thenReturn(Optional.of(sampleUser));
        when(roomRepository.findById(101L)).thenReturn(Optional.of(sampleRoom));
        when(roomRepository.isRoomAvailable(101L, checkIn, checkOut)).thenReturn(false); // Colliding booking!

        assertThrows(RoomUnavailableException.class, () ->
                bookingService.createBooking(request, "guest@example.com")
        );

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    @DisplayName("Should throw InvalidBookingException when check-out is on or before check-in")
    void testCreateBooking_InvalidDates() {
        LocalDate checkIn = LocalDate.now().plusDays(5);
        LocalDate checkOut = LocalDate.now().plusDays(3); // Invalid check-out!

        BookingRequest request = new BookingRequest();
        request.setHotelId(10L);
        request.setRoomId(101L);
        request.setCheckInDate(checkIn);
        request.setCheckOutDate(checkOut);
        request.setTotalGuests(2);

        when(userRepository.findByEmail("guest@example.com")).thenReturn(Optional.of(sampleUser));

        assertThrows(InvalidBookingException.class, () ->
                bookingService.createBooking(request, "guest@example.com")
        );
    }

    @Test
    @DisplayName("Should throw InvalidBookingException when total guests exceed room capacity")
    void testCreateBooking_ExceedCapacity() {
        LocalDate checkIn = LocalDate.now().plusDays(2);
        LocalDate checkOut = LocalDate.now().plusDays(4);

        BookingRequest request = new BookingRequest();
        request.setHotelId(10L);
        request.setRoomId(101L);
        request.setCheckInDate(checkIn);
        request.setCheckOutDate(checkOut);
        request.setTotalGuests(5); // Capacity is 2 adults + 1 child = 3

        when(userRepository.findByEmail("guest@example.com")).thenReturn(Optional.of(sampleUser));
        when(roomRepository.findById(101L)).thenReturn(Optional.of(sampleRoom));

        assertThrows(InvalidBookingException.class, () ->
                bookingService.createBooking(request, "guest@example.com")
        );
    }

    @Test
    @DisplayName("Should cancel reservation successfully and update payment status to REFUNDED")
    void testCancelBooking_Success() {
        Booking booking = new Booking("STY-2026-TEST1", sampleUser, sampleHotel,
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(4), 2, new BigDecimal("10000.00"), BookingStatus.CONFIRMED);
        booking.setId(99L);

        Payment payment = new Payment(booking, "TXN-TEST", new BigDecimal("10000.00"), PaymentMethod.CREDIT_CARD, PaymentStatus.SUCCESS);
        booking.setPayment(payment);

        when(bookingRepository.findById(99L)).thenReturn(Optional.of(booking));
        when(userRepository.findByEmail("guest@example.com")).thenReturn(Optional.of(sampleUser));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse response = bookingService.cancelBooking(99L, "guest@example.com");

        assertEquals(BookingStatus.CANCELLED, response.getBookingStatus());
        assertEquals("REFUNDED", response.getPaymentStatus());
        verify(bookingRepository, times(1)).save(booking);
    }
}
