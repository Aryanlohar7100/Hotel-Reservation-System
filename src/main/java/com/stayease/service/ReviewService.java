package com.stayease.service;

import com.stayease.dto.request.ReviewRequest;
import com.stayease.entity.Review;
import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByHotel(Long hotelId);
    Review addReview(ReviewRequest request, String userEmail);
}
