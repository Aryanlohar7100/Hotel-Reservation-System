package com.stayease.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "hotel_amenities", indexes = {
    @Index(name = "idx_hotel_amenity_hotel", columnList = "hotel_id"),
    @Index(name = "idx_hotel_amenity_amenity", columnList = "amenity_id")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_hotel_amenity", columnNames = {"hotel_id", "amenity_id"})
})
public class HotelAmenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false)
    @JsonIgnoreProperties({"rooms", "reviews", "hotelAmenities"})
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenity amenity;

    @Column(name = "is_complimentary", nullable = false)
    private boolean complimentary = true;

    @Column(length = 255)
    private String details;

    public HotelAmenity() {
    }

    public HotelAmenity(Hotel hotel, Amenity amenity, boolean complimentary, String details) {
        this.hotel = hotel;
        this.amenity = amenity;
        this.complimentary = complimentary;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Amenity getAmenity() {
        return amenity;
    }

    public void setAmenity(Amenity amenity) {
        this.amenity = amenity;
    }

    public boolean isComplimentary() {
        return complimentary;
    }

    public void setComplimentary(boolean complimentary) {
        this.complimentary = complimentary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
