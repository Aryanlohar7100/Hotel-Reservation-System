package com.stayease.service.impl;

import com.stayease.dto.request.ReviewRequest;
import com.stayease.entity.Hotel;
import com.stayease.entity.Review;
import com.stayease.entity.User;
import com.stayease.exception.ResourceNotFoundException;
import com.stayease.repository.HotelRepository;
import com.stayease.repository.ReviewRepository;
import com.stayease.repository.UserRepository;
import com.stayease.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             HotelRepository hotelRepository,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Review> getReviewsByHotel(Long hotelId) {
        return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId);
    }

    @Override
    @Transactional
    public Review addReview(ReviewRequest request, String userEmail) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: " + request.getHotelId()));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Review review = new Review();
        review.setHotel(hotel);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment().trim());
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepository.save(review);

        // Recalculate average star rating
        Double avg = reviewRepository.findAverageRatingByHotelId(hotel.getId());
        if (avg != null) {
            BigDecimal rounded = BigDecimal.valueOf(avg).setScale(1, RoundingMode.HALF_UP);
            hotel.setStarRating(rounded.doubleValue());
            hotelRepository.save(hotel);
        }

        return saved;
    }
}
