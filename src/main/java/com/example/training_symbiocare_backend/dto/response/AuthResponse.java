package com.example.training_symbiocare_backend.dto.response;

import com.example.training_symbiocare_backend.security.jwt.UserItem;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse {

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

    @NotNull
    private UserItem user ;




}
