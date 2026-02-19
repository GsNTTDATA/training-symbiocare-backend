package com.example.training_symbiocare_backend.models;

import com.example.training_symbiocare_backend.models.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "invite")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String cf;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String cap;

    @Column(nullable = false)
    private String province;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false)
    private String bloodType;

    @Column(nullable = false)
    private String level;

    @Column(nullable = false)
    private String sport;

    @Column(nullable = false, columnDefinition = "text[]")
    private String[] pathologies;

    @Column(nullable = false, columnDefinition = "text[]")
    private String[] medications;

    @Column(nullable = false, columnDefinition = "text[]")
    private String[] injuries;

    @Column(nullable = false)
    @Builder.Default
    private Boolean used = false;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "doctorUserId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "patientId", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Patient patient;
}
