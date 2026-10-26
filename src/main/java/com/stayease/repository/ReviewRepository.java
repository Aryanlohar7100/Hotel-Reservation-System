package com.stayease.repository;

import com.stayease.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r JOIN FETCH r.user WHERE r.hotel.id = :hotelId ORDER BY r.createdAt DESC")
    List<Review> findByHotelIdOrderByCreatedAtDesc(@Param("hotelId") Long hotelId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.hotel.id = :hotelId")
    Double findAverageRatingByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.hotel.id = :hotelId")
    long countByHotelId(@Param("hotelId") Long hotelId);
}
