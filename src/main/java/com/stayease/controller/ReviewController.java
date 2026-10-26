package com.stayease.controller;

import com.stayease.dto.request.ReviewRequest;
import com.stayease.dto.response.ApiResponse;
import com.stayease.entity.Review;
import com.stayease.security.SecurityUtils;
import com.stayease.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByHotel(@PathVariable Long hotelId) {
        List<Review> reviews = reviewService.getReviewsByHotel(hotelId);
        return ResponseEntity.ok(ApiResponse.ok(reviews));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Review>> addReview(@Valid @RequestBody ReviewRequest request) {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new AccessDeniedException("User must be logged in to leave a review"));

        Review review = reviewService.addReview(request, email);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Review submitted successfully!", review));
    }
}
