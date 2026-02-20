package com.example.training_symbiocare_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "medical_detection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private OffsetDateTime date;

    @ManyToOne
    @JoinColumn(name = "\"patientId\"")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Patient patient;
}
