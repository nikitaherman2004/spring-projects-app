package com.project_service.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderService {

    private final PasswordEncoder passwordEncoder;


    public boolean matchesPassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}