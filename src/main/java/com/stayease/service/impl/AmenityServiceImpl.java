package com.stayease.service.impl;

import com.stayease.entity.Amenity;
import com.stayease.entity.Hotel;
import com.stayease.entity.HotelAmenity;
import com.stayease.exception.ResourceNotFoundException;
import com.stayease.repository.AmenityRepository;
import com.stayease.repository.HotelAmenityRepository;
import com.stayease.repository.HotelRepository;
import com.stayease.service.AmenityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepository amenityRepository;
    private final HotelAmenityRepository hotelAmenityRepository;
    private final HotelRepository hotelRepository;

    public AmenityServiceImpl(AmenityRepository amenityRepository,
                              HotelAmenityRepository hotelAmenityRepository,
                              HotelRepository hotelRepository) {
        this.amenityRepository = amenityRepository;
        this.hotelAmenityRepository = hotelAmenityRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return amenityRepository.findAll();
    }

    @Override
    @Transactional
    public Amenity createAmenity(String name, String icon, String description) {
        return amenityRepository.findByNameIgnoreCase(name.trim())
                .orElseGet(() -> amenityRepository.save(new Amenity(
                        name.trim(),
                        icon != null ? icon : "bi-check-circle-fill",
                        description
                )));
    }

    @Override
    public List<HotelAmenity> getHotelAmenities(Long hotelId) {
        return hotelAmenityRepository.findByHotelId(hotelId);
    }

    @Override
    @Transactional
    public HotelAmenity attachAmenityToHotel(Long hotelId, Long amenityId, boolean isComplimentary, String details) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + hotelId));
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found with ID: " + amenityId));

        return hotelAmenityRepository.findByHotelIdAndAmenityId(hotelId, amenityId)
                .map(existing -> {
                    existing.setComplimentary(isComplimentary);
                    existing.setDetails(details);
                    return hotelAmenityRepository.save(existing);
                })
                .orElseGet(() -> hotelAmenityRepository.save(new HotelAmenity(hotel, amenity, isComplimentary, details)));
    }

    @Override
    @Transactional
    public void removeAmenityFromHotel(Long hotelId, Long amenityId) {
        hotelAmenityRepository.deleteByHotelIdAndAmenityId(hotelId, amenityId);
    }
}
