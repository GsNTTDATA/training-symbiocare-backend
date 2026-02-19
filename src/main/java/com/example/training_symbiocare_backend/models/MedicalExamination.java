package com.example.training_symbiocare_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medical_examination")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String motivation;

    @Column(nullable = false)
    private String note;

    @ManyToOne
    @JoinColumn(name = "patientId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctorUserId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Doctor doctor;

    @OneToOne
    @JoinColumn(name = "reservationId", unique = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Reservation reservation;
}
