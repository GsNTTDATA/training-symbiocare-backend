package com.example.training_symbiocare_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class SignInDTO {

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
