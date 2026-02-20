package com.example.training_symbiocare_backend.dto.response;

import com.example.training_symbiocare_backend.security.jwt.UserItem;
import lombok.Builder;
import lombok.Getter;

@Builder
public class AuthResult {
    @Getter
    private final AuthResponse authResponse;
    @Getter
    private final TwoFactorResponse twoFactorResponse;

    @Getter
    private final UserItem user;

    private AuthResult(AuthResponse authResponse, TwoFactorResponse twoFactorResponse, UserItem user) {
        this.authResponse = authResponse;
        this.twoFactorResponse = twoFactorResponse;
        this.user = user;
    }

    public static AuthResult withAuth(AuthResponse response) {
        return new AuthResult(response, null, null);
    }

    public static AuthResult withTwoFactor(TwoFactorResponse response) {
        return new AuthResult(null, response, null);
    }
    public boolean isAuthResponse() {
        return authResponse != null;
    }

}
