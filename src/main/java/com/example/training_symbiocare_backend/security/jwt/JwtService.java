package com.example.training_symbiocare_backend.security.jwt;

import com.example.training_symbiocare_backend.dto.response.DeviceInfo;
import com.example.training_symbiocare_backend.models.Session;
import com.example.training_symbiocare_backend.models.User;
import com.example.training_symbiocare_backend.repositories.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    // Getter per expiration (usato nel filtro per impostare cookie)
    @Getter
    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Getter
    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;

    public JwtService(SessionRepository sessionRepository, PasswordEncoder passwordEncoder) {
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Genera Access Token (5-15 min)
    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());

        return createToken(claims, user.getId().toString(), accessTokenExpiration);
    }

    // Genera Refresh Token (7-30 giorni) e crea sessione associata
    @Transactional
    public String generateRefreshToken(User user, DeviceInfo deviceInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());

        // Crea nuova sessione (ID sarà generato automaticamente da Hibernate)
        Session session = Session.builder()
                .user(user)
                .deviceInfo(deviceInfo.getUserAgent())
                .ipAddress(deviceInfo.getIpAddress())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();

        // Salva la session per ottenere l'ID generato (flush immediato)
        session = sessionRepository.saveAndFlush(session);

        // Ora aggiungi il sessionId al token
        claims.put("sessionId", session.getId().toString());

        String refreshToken = createToken(claims, user.getId().toString(), refreshTokenExpiration);

        String hashedRefreshToken = this.hashToken(refreshToken);

        // Aggiorna la session con il refresh token hashato
        session.setRefreshToken(hashedRefreshToken);
        // Non serve un altro save() - Hibernate rileva automaticamente il change (dirty checking)
        // Il flush avverrà al commit della transazione

        return refreshToken;
    }

    // Crea token JWT
    private String createToken(Map<String, Object> claims, String subject, Long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public boolean validateToken(String token, String id) {
        try {
            // extractUserId usa Jwts.parser() che VERIFICA la firma
            // Se la firma è invalida, lancia un'eccezione
            final String tokenId = extractUserId(token);

            // Verifica che l'ID corrisponda e che non sia scaduto
            return (tokenId.equals(id) && !isTokenExpired(token));

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token scaduto
            return false;
        } catch (io.jsonwebtoken.security.SignatureException e) {
            // Firma non valida - token manomesso!
            return false;
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // Token malformato
            return false;
        } catch (Exception e) {
            // Qualsiasi altro errore JWT
            return false;
        }
    }

    public String extractSessionId(String token) {
        return extractClaim(token, claims -> claims.get("sessionId", String.class));
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", String.class));
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing token", e);
        }
    }
}

