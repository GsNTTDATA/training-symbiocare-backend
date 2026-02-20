package com.example.training_symbiocare_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "session")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "\"userId\"")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(columnDefinition = "TEXT", name = "\"refreshToken\"")
    private String refreshToken;

    @Column(nullable = false, name = "\"deviceInfo\"")
    private String deviceInfo;

    @Column(nullable = false, name = "\"ipAddress\"")
    private String ipAddress;

    @Column(nullable = false, name = "\"createdAt\"")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "\"expiresAt\"")
    private LocalDateTime expiresAt;
}
