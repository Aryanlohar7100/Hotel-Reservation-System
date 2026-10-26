package com.stayease.repository;

import com.stayease.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByActiveTrue();

    @Query("SELECT DISTINCT h FROM Hotel h " +
           "WHERE h.active = true " +
           "AND (:city IS NULL OR LOWER(h.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:minRating IS NULL OR h.starRating >= :minRating) " +
           "AND (:keyword IS NULL OR LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(h.address) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "     OR LOWER(h.city) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Hotel> searchHotels(
            @Param("city") String city,
            @Param("minRating") Double minRating,
            @Param("keyword") String keyword
    );

    @Query("SELECT DISTINCT h.city FROM Hotel h WHERE h.active = true ORDER BY h.city ASC")
    List<String> findDistinctCities();
}
