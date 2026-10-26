package com.stayease.service.impl;

import com.stayease.dto.request.LoginRequest;
import com.stayease.dto.request.RegisterRequest;
import com.stayease.dto.response.AuthResponse;
import com.stayease.entity.Role;
import com.stayease.entity.User;
import com.stayease.exception.InvalidBookingException;
import com.stayease.exception.ResourceNotFoundException;
import com.stayease.repository.UserRepository;
import com.stayease.security.SecurityUtils;
import com.stayease.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail().trim().toLowerCase())) {
            throw new InvalidBookingException("Email is already registered: " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName().trim());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.ROLE_CUSTOMER);
        user.setActive(true);

        User saved = userRepository.save(user);

        return new AuthResponse(saved.getId(), saved.getEmail(), saved.getFullName(), saved.getRole(),
                "Registration successful. Please log in.");
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new AuthResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole(),
                "Login successful");
    }

    @Override
    public User getCurrentAuthenticatedUser() {
        String email = SecurityUtils.getCurrentUserEmail()
                .orElseThrow(() -> new ResourceNotFoundException("No active session found. Please log in."));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for session: " + email));
    }

    @Override
    @Transactional
    public AuthResponse updateProfile(com.stayease.dto.request.ProfileUpdateRequest request) {
        User user = getCurrentAuthenticatedUser();
        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber().trim());
        }
        if (request.getNewPassword() != null && !request.getNewPassword().isBlank()) {
            if (request.getNewPassword().length() < 6) {
                throw new com.stayease.exception.InvalidBookingException("Password must be at least 6 characters");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User updated = userRepository.save(user);
        return new AuthResponse(updated.getId(), updated.getEmail(), updated.getFullName(), updated.getRole(), "Profile updated successfully");
    }
}
