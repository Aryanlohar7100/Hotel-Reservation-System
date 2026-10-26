package com.stayease.dto.response;

import com.stayease.entity.Booking;
import com.stayease.entity.BookingItem;
import com.stayease.entity.BookingStatus;
import com.stayease.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {
    private Long id;
    private String bookingReference;
    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelCity;
    private String hotelImage;
    private Long roomId;
    private String roomNumber;
    private String roomType;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private long totalNights;
    private Integer totalGuests;
    private BigDecimal pricePerNight;
    private BigDecimal totalAmount;
    private BookingStatus bookingStatus;
    private String customerName;
    private String customerEmail;
    private String paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    public static BookingResponse fromEntity(Booking booking) {
        BookingResponse dto = new BookingResponse();
        dto.setId(booking.getId());
        dto.setBookingReference(booking.getBookingReference());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalGuests(booking.getTotalGuests());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setCreatedAt(booking.getCreatedAt());

        if (booking.getUser() != null) {
            dto.setCustomerName(booking.getUser().getFullName());
            dto.setCustomerEmail(booking.getUser().getEmail());
        }

        if (booking.getHotel() != null) {
            dto.setHotelId(booking.getHotel().getId());
            dto.setHotelName(booking.getHotel().getName());
            dto.setHotelAddress(booking.getHotel().getAddress());
            dto.setHotelCity(booking.getHotel().getCity());
            dto.setHotelImage(booking.getHotel().getImageUrl());
        }

        if (booking.getItems() != null && !booking.getItems().isEmpty()) {
            BookingItem firstItem = booking.getItems().get(0);
            dto.setTotalNights(firstItem.getNightsCount());
            dto.setPricePerNight(firstItem.getPricePerNight());
            if (firstItem.getRoom() != null) {
                dto.setRoomId(firstItem.getRoom().getId());
                dto.setRoomNumber(firstItem.getRoom().getRoomNumber());
                dto.setRoomType(firstItem.getRoom().getRoomType() != null ? firstItem.getRoom().getRoomType().getDisplayName() : "");
            }
        }

        Payment payment = booking.getPayment();
        if (payment != null) {
            dto.setPaymentStatus(payment.getPaymentStatus() != null ? payment.getPaymentStatus().name() : "PENDING");
            dto.setPaymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().getLabel() : "");
            dto.setTransactionId(payment.getTransactionId());
        } else {
            dto.setPaymentStatus("PENDING");
        }

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Long getHotelId() {
        return hotelId;
    }

    public void setHotelId(Long hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public long getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(long totalNights) {
        this.totalNights = totalNights;
    }

    public Integer getTotalGuests() {
        return totalGuests;
    }

    public void setTotalGuests(Integer totalGuests) {
        this.totalGuests = totalGuests;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
