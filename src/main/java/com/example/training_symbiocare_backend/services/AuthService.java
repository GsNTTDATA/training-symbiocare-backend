package com.example.training_symbiocare_backend.services;


import com.example.training_symbiocare_backend.dto.request.SignInDTO;
import com.example.training_symbiocare_backend.dto.response.AuthResponse;
import com.example.training_symbiocare_backend.dto.response.AuthResult;
import com.example.training_symbiocare_backend.dto.response.DeviceInfo;
import com.example.training_symbiocare_backend.dto.response.TwoFactorResponse;
import com.example.training_symbiocare_backend.exceptions.UserNotFoundException;
import com.example.training_symbiocare_backend.models.AuthChallenge;
import com.example.training_symbiocare_backend.models.Session;
import com.example.training_symbiocare_backend.models.User;
import com.example.training_symbiocare_backend.models.enums.UserRole;
import com.example.training_symbiocare_backend.repositories.*;
import com.example.training_symbiocare_backend.security.cookie.CookieService;
import com.example.training_symbiocare_backend.security.jwt.JwtService;
import com.example.training_symbiocare_backend.security.jwt.UserItem;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class AuthService {


    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AuthChallengeRepository authChallengeRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CookieService cookieService;
    private final TwoFactorService twoFactorService;
    private final AuthenticationManager authenticationManager;


    public AuthService(UserRepository userRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, AuthChallengeRepository authChallengeRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder, JwtService jwtService, CookieService cookieService, TwoFactorService twoFactorService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.authChallengeRepository = authChallengeRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.cookieService = cookieService;
        this.twoFactorService = twoFactorService;
        this.authenticationManager = authenticationManager;
    }



    public AuthResult signIn (SignInDTO signInDTO, DeviceInfo deviceInfo) {

        String email = signInDTO.getEmail();
        String password = signInDTO.getPassword();

        User user = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException("User not found"));

        if( !passwordEncoder.matches(password, user.getPassword()) ) {
            throw new RuntimeException("Invalid credentials");
        }

        if(!user.getTwoFactorEnabled())
            return completeLogin(user, deviceInfo);

        AuthChallenge authChallenge = this.authChallengeRepository.save(
                AuthChallenge.builder()
                        .user(user)
                        .type("LOGIN_2FA")
                        .expiresAt(twoFactorService.calculateExpiryDate(5))
                        .attempts(0)
                        .maxAttempts(5)
                        .build()
        );

        TwoFactorResponse twoFactorResponse =  new TwoFactorResponse(
                true,
                authChallenge.getChallengeId()
        );

        return AuthResult.withTwoFactor(twoFactorResponse);
    }

    private AuthResult completeLogin (User user, DeviceInfo deviceInfo) {
        UserItem finalUser = new UserItem(user);

        if(user.getRole() == UserRole.DOCTOR) {
            finalUser.setDoctor(doctorRepository.findByUserId(user.getId()).orElseThrow(()-> new UserNotFoundException("Doctor's info not found")));
        } else if(user.getRole() == UserRole.PATIENT) {
            finalUser.setPatient(patientRepository.findByUserId(user.getId()).orElseThrow(()-> new UserNotFoundException("Patient's info not found")));
        }

        String accessToken = jwtService.generateAccessToken(finalUser.getUser());
        String refreshToken = jwtService.generateRefreshToken(finalUser.getUser(), deviceInfo);

        AuthResponse authResponse = new AuthResponse(
                accessToken,
                refreshToken,
                finalUser
        );

        return AuthResult.builder().authResponse(authResponse).build();
    }

}

