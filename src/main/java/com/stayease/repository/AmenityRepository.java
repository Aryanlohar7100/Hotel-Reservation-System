package com.stayease.repository;

import com.stayease.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    Optional<Amenity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
