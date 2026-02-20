package com.example.training_symbiocare_backend.security.jwt;

import com.example.training_symbiocare_backend.models.Doctor;
import com.example.training_symbiocare_backend.models.Patient;
import com.example.training_symbiocare_backend.models.User;
import com.example.training_symbiocare_backend.models.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserItem {
    private User user;

    private Doctor doctor;

    private Patient patient;


    public UserItem (User user, Patient patient) {
        this.user = user;
        this.patient = patient;
    }

    public UserItem (User user , Doctor doctor) {
        this.user = user;
        this.doctor = doctor;
    }

    public UserItem (User user) {
        this.user = user;
    }

}
