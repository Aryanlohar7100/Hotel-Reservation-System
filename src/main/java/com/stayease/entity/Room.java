package com.stayease.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "rooms", indexes = {
    @Index(name = "idx_rooms_hotel_id", columnList = "hotel_id"),
    @Index(name = "idx_rooms_type", columnList = "room_type"),
    @Index(name = "idx_rooms_active", columnList = "active")
})
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnore
    private Hotel hotel;

    @NotBlank(message = "Room number is required")
    @Column(name = "room_number", nullable = false, length = 30)
    private String roomNumber;

    @NotNull(message = "Room type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false, length = 30)
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Column(name = "base_price_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerNight;

    @Min(value = 1, message = "Adult capacity must be at least 1")
    @Column(name = "capacity_adults", nullable = false)
    private Integer capacityAdults = 2;

    @Min(value = 0, message = "Children capacity cannot be negative")
    @Column(name = "capacity_children", nullable = false)
    private Integer capacityChildren = 1;

    @Column(columnDefinition = "TEXT")
    private String amenities = "Free High-Speed WiFi, Air Conditioning, Smart LED TV, En-suite Bathroom, Complimentary Breakfast";

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    public Room() {
    }

    public Room(Hotel hotel, String roomNumber, RoomType roomType, BigDecimal basePricePerNight,
                Integer capacityAdults, Integer capacityChildren, String amenities, String imageUrl) {
        this.hotel = hotel;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.basePricePerNight = basePricePerNight;
        this.capacityAdults = capacityAdults;
        this.capacityChildren = capacityChildren;
        this.amenities = amenities;
        this.imageUrl = imageUrl;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Transient
    @JsonProperty("hotelId")
    public Long getHotelId() {
        return hotel != null ? hotel.getId() : null;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public BigDecimal getBasePricePerNight() {
        return basePricePerNight;
    }

    public void setBasePricePerNight(BigDecimal basePricePerNight) {
        this.basePricePerNight = basePricePerNight;
    }

    public Integer getCapacityAdults() {
        return capacityAdults;
    }

    public void setCapacityAdults(Integer capacityAdults) {
        this.capacityAdults = capacityAdults;
    }

    public Integer getCapacityChildren() {
        return capacityChildren;
    }

    public void setCapacityChildren(Integer capacityChildren) {
        this.capacityChildren = capacityChildren;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
