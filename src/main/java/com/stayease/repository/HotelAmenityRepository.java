package com.stayease.repository;

import com.stayease.entity.HotelAmenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelAmenityRepository extends JpaRepository<HotelAmenity, Long> {
    List<HotelAmenity> findByHotelId(Long hotelId);
    Optional<HotelAmenity> findByHotelIdAndAmenityId(Long hotelId, Long amenityId);
    void deleteByHotelIdAndAmenityId(Long hotelId, Long amenityId);
}
