package com.example.training_symbiocare_backend.services;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TwoFactorService {
    public LocalDateTime calculateExpiryDate(int i) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(i);
    }
}
