package com.example.training_symbiocare_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @Column(name = "\"userId\"")
    private UUID userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "\"userId\"")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User user;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false, name = "\"medicalOffice\"")
    private String medicalOffice;

    @Column(nullable = false, name = "\"registrationNumber\"")
    private String registrationNumber;

    @Column(nullable = false, name = "\"orderProvince\"")
    private String orderProvince;

    @Column(nullable = false, name = "\"orderDate\"")
    private LocalDate orderDate;

    @Column(nullable = false, name = "\"orderType\"")
    private String orderType;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Patient> patients = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Availability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Invite> invites = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<MedicalExamination> medicalExaminations = new ArrayList<>();
}
