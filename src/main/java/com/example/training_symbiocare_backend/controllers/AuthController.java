package com.example.training_symbiocare_backend.controllers;

import com.example.training_symbiocare_backend.dto.request.SignInDTO;
import com.example.training_symbiocare_backend.dto.response.AuthResult;
import com.example.training_symbiocare_backend.dto.response.DeviceInfo;
import com.example.training_symbiocare_backend.security.cookie.CookieService;
import com.example.training_symbiocare_backend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final CookieService cookieService;


    public AuthController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }


    // POST Login endpoint
    @PostMapping("/signin")
    public AuthResult signing(@RequestBody SignInDTO signInDto, HttpServletRequest request, HttpServletResponse response) {

         AuthResult authResult = authService.signIn(signInDto, deviceInfoExtractor(request));

         if(!authResult.isAuthResponse())
             return authResult;

        ResponseCookie accessCookie = cookieService.createAccessTokenCookie(authResult.getAuthResponse().getAccessToken());
        ResponseCookie refreshCookie = cookieService.createRefreshTokenCookie(authResult.getAuthResponse().getRefreshToken());

        // Aggiungili all'header della risposta
        response.addHeader("Set-Cookie", accessCookie.toString());
        response.addHeader("Set-Cookie", refreshCookie.toString());
        return AuthResult.builder().user(authResult.getUser()).build();
    }

    // POST Register endpoint

    // POST Logout endpoint

    // POST  Verify otp endpoint

    // POST set 2fa endpoint

    // POST confirm 2fa endpoint

    // POST disable 2fa endpoint

    private DeviceInfo deviceInfoExtractor (HttpServletRequest request) {
        return new DeviceInfo(request.getRemoteAddr(), request.getHeader("User-Agent"));
    }
}
