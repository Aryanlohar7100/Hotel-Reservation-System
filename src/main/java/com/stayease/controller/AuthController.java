package com.stayease.controller;

import com.stayease.dto.request.LoginRequest;
import com.stayease.dto.request.RegisterRequest;
import com.stayease.dto.response.ApiResponse;
import com.stayease.dto.response.AuthResponse;
import com.stayease.entity.User;
import com.stayease.security.SecurityUtils;
import com.stayease.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.ok("Registration successful. Please log in.", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        AuthResponse response = authService.login(request);
        // Ensure session creation for cookie-based state persistence
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(ApiResponse.ok("Logged out successfully", null));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse>> getCurrentUser() {
        if (SecurityUtils.getCurrentUserEmail().isEmpty()) {
            return ResponseEntity.ok(ApiResponse.ok(null));
        }
        User user = authService.getCurrentAuthenticatedUser();
        AuthResponse response = new AuthResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole(), "Authenticated");
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<AuthResponse>> updateProfile(@Valid @RequestBody com.stayease.dto.request.ProfileUpdateRequest request) {
        AuthResponse updated = authService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", updated));
    }
}
