package com.example.training_symbiocare_backend.models;

import com.example.training_symbiocare_backend.models.enums.PatientLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "userId", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctorUserId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Doctor doctor;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(nullable = false, name = "\"bloodType\"")
    private String bloodType;

    @Column(nullable = false)
    private String sport;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PatientLevel level = PatientLevel.BEGINNER;

    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> pathologies;

    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> medications;

    @Column(nullable = false, columnDefinition = "text[]")
    private List<String> injuries;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<MedicalExamination> medicalExaminations = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<MedicalDetection> medicalDetections = new ArrayList<>();
}
