package com.example.training_symbiocare_backend.repositories;

import com.example.training_symbiocare_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCf(String cf);

    boolean existsByEmail(String email);

    boolean existsByCf(String cf);
}