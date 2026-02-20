package com.example.training_symbiocare_backend.repositories;

import com.example.training_symbiocare_backend.models.AuthChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthChallengeRepository extends JpaRepository<AuthChallenge, UUID> {


}
