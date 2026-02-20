package com.example.training_symbiocare_backend.services;

import com.example.training_symbiocare_backend.exceptions.UserNotFoundException;
import com.example.training_symbiocare_backend.models.User;
import com.example.training_symbiocare_backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Carica utente per email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con email: " + email));
    }

    // Carica utente per ID
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con ID: " + id));
    }

    // Carica utente per CF
    public User getUserByCf(String cf) {
        return userRepository.findByCf(cf)
                .orElseThrow(() -> new UserNotFoundException("Utente non trovato con CF: " + cf));
    }

    // Verifica se email esiste
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // Verifica se CF esiste
    public boolean existsByCf(String cf) {
        return userRepository.existsByCf(cf);
    }
}
