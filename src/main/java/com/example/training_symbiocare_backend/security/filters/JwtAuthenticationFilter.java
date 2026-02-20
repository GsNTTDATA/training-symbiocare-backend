package com.example.training_symbiocare_backend.security.filters;

import com.example.training_symbiocare_backend.exceptions.UnauthorizedException;
import com.example.training_symbiocare_backend.models.Session;
import com.example.training_symbiocare_backend.models.User;
import com.example.training_symbiocare_backend.repositories.SessionRepository;
import com.example.training_symbiocare_backend.security.CustomUserDetailsService;
import com.example.training_symbiocare_backend.security.cookie.CookieService;
import com.example.training_symbiocare_backend.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String accessToken = extractTokenFromCookies(request, "accessToken");
        String refreshToken = extractTokenFromCookies(request, "refreshToken");

        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken != null) {
            if (isTokenValid(accessToken, request)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        if (refreshToken != null) {
            refreshAccessToken(refreshToken, response);
        }

        filterChain.doFilter(request, response);
    }





    // Estrae token dal cookie
    private String extractTokenFromCookies(HttpServletRequest request, String tokenType) {
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if (tokenType.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void refreshAccessToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new UnauthorizedException("Refresh token mancante");
        }

        try {
            String sessionId = jwtService.extractSessionId(refreshToken);
            String userId = jwtService.extractUserId(refreshToken);

            if (sessionId == null || userId == null) {
                throw new UnauthorizedException("Refresh token non valido");
            }

            // Valida il refresh token (verifica firma e scadenza)
            if (!jwtService.validateToken(refreshToken, userId)) {
                throw new UnauthorizedException("Refresh token scaduto o non valido");
            }

            // Carica la sessione dal database
            Session session = sessionRepository.findById(UUID.fromString(sessionId))
                    .orElseThrow(() -> new UnauthorizedException("Sessione non trovata"));

            // Verifica che il refresh token hashato corrisponda
            if (!passwordEncoder.matches(refreshToken, session.getRefreshToken())) {
                throw new UnauthorizedException("Refresh token non corrisponde alla sessione");
            }

            // Verifica che la sessione non sia scaduta
            if (session.getExpiresAt().isBefore(java.time.LocalDateTime.now())) {
                sessionRepository.delete(session);
                throw new UnauthorizedException("Sessione scaduta");
            }

            // Carica l'utente
            User user = session.getUser();
            if (user == null) {
                throw new UnauthorizedException("Utente non trovato");
            }

            // Genera nuovo access token
            String newAccessToken = jwtService.generateAccessToken(user);

            // Imposta il cookie nella response
            response.addHeader("Set-Cookie", cookieService.createAccessTokenCookie(newAccessToken).toString());

        } catch (UnauthorizedException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Errore durante refresh token", e);
            throw new UnauthorizedException("Errore durante il refresh del token");
        }
    }

    private boolean isTokenValid(String token, HttpServletRequest request) {
        try {
            String id = jwtService.extractUserId(token);

            if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(id);

                if (jwtService.validateToken(token, id)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            logger.error("Errore autenticazione JWT", e);
            return false;
        }
    }

}
