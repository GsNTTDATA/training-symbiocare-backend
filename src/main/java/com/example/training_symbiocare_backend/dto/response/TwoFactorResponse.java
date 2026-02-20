package com.example.training_symbiocare_backend.dto.response;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TwoFactorResponse {

    @NotNull
    private boolean required2fa;

    @NotNull
    private String challengeId;


}
