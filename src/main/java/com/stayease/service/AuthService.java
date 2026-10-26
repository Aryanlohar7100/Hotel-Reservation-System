package com.stayease.service;

import com.stayease.dto.request.LoginRequest;
import com.stayease.dto.request.RegisterRequest;
import com.stayease.dto.response.AuthResponse;
import com.stayease.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    User getCurrentAuthenticatedUser();
    AuthResponse updateProfile(com.stayease.dto.request.ProfileUpdateRequest request);
}
