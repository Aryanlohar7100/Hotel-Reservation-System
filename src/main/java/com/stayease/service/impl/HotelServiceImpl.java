package com.stayease.service.impl;

import com.stayease.dto.request.HotelCreateRequest;
import com.stayease.entity.Hotel;
import com.stayease.exception.ResourceNotFoundException;
import com.stayease.repository.HotelRepository;
import com.stayease.service.HotelService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAllActiveHotels() {
        return hotelRepository.findByActiveTrue();
    }

    @Override
    public List<Hotel> searchHotels(String city, Double minRating, String keyword) {
        String cityParam = (city != null && !city.isBlank()) ? city.trim() : null;
        String keywordParam = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;
        return hotelRepository.searchHotels(cityParam, minRating, keywordParam);
    }

    @Override
    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + id));
    }

    @Override
    @Transactional
    public Hotel createHotel(HotelCreateRequest request) {
        Hotel hotel = new Hotel();
        hotel.setName(request.getName().trim());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress().trim());
        hotel.setCity(request.getCity().trim());
        hotel.setState(request.getState() != null ? request.getState().trim() : "");
        hotel.setCountry(request.getCountry() != null ? request.getCountry().trim() : "India");
        hotel.setContactEmail(request.getContactEmail());
        hotel.setContactPhone(request.getContactPhone());
        hotel.setStarRating(request.getStarRating() != null ? request.getStarRating() : 4.5);
        hotel.setImageUrl(request.getImageUrl());
        hotel.setActive(true);
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public Hotel updateHotel(Long id, HotelCreateRequest request) {
        Hotel hotel = getHotelById(id);
        hotel.setName(request.getName().trim());
        hotel.setDescription(request.getDescription());
        hotel.setAddress(request.getAddress().trim());
        hotel.setCity(request.getCity().trim());
        hotel.setState(request.getState());
        hotel.setCountry(request.getCountry());
        hotel.setContactEmail(request.getContactEmail());
        hotel.setContactPhone(request.getContactPhone());
        if (request.getStarRating() != null) {
            hotel.setStarRating(request.getStarRating());
        }
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            hotel.setImageUrl(request.getImageUrl());
        }
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = getHotelById(id);
        hotel.setActive(false);
        hotelRepository.save(hotel);
    }

    @Override
    public List<String> getAvailableCities() {
        return hotelRepository.findDistinctCities();
    }
}
