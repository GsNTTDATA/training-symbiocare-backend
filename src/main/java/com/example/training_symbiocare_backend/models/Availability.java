package com.example.training_symbiocare_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "availability")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true, name = "\"startTime\"")
    private OffsetDateTime startTime;

    @Column(nullable = false, unique = true, name = "\"endTime\"")
    private OffsetDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "\"doctorUserId\"")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Doctor doctor;
}
