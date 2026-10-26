package com.stayease.service;

import com.stayease.dto.request.RegisterRequest;
import com.stayease.dto.response.AuthResponse;
import com.stayease.entity.Role;
import com.stayease.entity.User;
import com.stayease.exception.InvalidBookingException;
import com.stayease.repository.UserRepository;
import com.stayease.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Should successfully register customer with hashed password and ROLE_CUSTOMER")
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("Alice Wonderland", "alice@example.com", "secretPass123", "9988776655");

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secretPass123")).thenReturn("$2a$10$hashedPasswordHere");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(42L);
            return u;
        });

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(42L, response.getId());
        assertEquals("alice@example.com", response.getEmail());
        assertEquals(Role.ROLE_CUSTOMER, response.getRole());
        assertEquals("Alice Wonderland", response.getFullName());

        verify(passwordEncoder, times(1)).encode("secretPass123");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should reject registration when email already exists")
    void testRegister_DuplicateEmail() {
        RegisterRequest request = new RegisterRequest("Alice Wonderland", "existing@example.com", "secretPass123", "9988776655");

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(InvalidBookingException.class, () ->
                authService.register(request)
        );

        verify(userRepository, never()).save(any(User.class));
    }
}
