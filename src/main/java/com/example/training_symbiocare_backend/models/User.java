package com.example.training_symbiocare_backend.models;

import com.example.training_symbiocare_backend.models.enums.Gender;
import com.example.training_symbiocare_backend.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String cf;

    @Column(nullable = false, name = "\"birthDate\"")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String cap;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false, name = "\"twoFactorEnabled\"")
    @Builder.Default
    private Boolean twoFactorEnabled = false;

    @Column(columnDefinition = "TEXT", name = "\"twoFactorSecret\"")
    private String twoFactorSecret;

    @Column(columnDefinition = "TEXT", name = "\"twoFactorSecretPending\"")
    private String twoFactorSecretPending;

    @Column(nullable = false, name = "\"mustChangePassword\"")
    @Builder.Default
    private Boolean mustChangePassword = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<AuthChallenge> authChallenges = new ArrayList<>();
}
