package cires.bemodule.security.services;

import cires.bemodule.dtos.responses.AuthResponse;
import cires.bemodule.dtos.requests.LoginRequest;
import cires.bemodule.dtos.requests.RefreshTokenRequest;
import cires.bemodule.exceptions.securityexceptions.InvalidJwtTokenException;
import cires.bemodule.security.models.UserPrincipal;
import cires.bemodule.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private static final String REFRESH_TOKEN_TYPE = "refresh";

    public AuthResponse login(LoginRequest request) {

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
            String jwtToken = jwtService.generateJwtToken(userPrincipal);
            String refreshToken = jwtService.generateRefreshJwtToken(userPrincipal);

            return new AuthResponse(jwtToken, refreshToken, "Bearer", jwtExpiration);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            log.warn("Login failed for user: {}", request.getUsername());
            throw e;
        } catch (AuthenticationException e) {
            log.error("Authentication error for user: {}", request.getUsername(), e);
            throw e;
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        final String refreshToken = request.getRefreshToken();
        final String username;

        // Validate token type
        if (!REFRESH_TOKEN_TYPE.equals(jwtService.extractTokenType(refreshToken))) {
            throw new InvalidJwtTokenException("Invalid token type – expected refresh token");
        }

        // Validate token (signature, expiration, etc.)
        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new InvalidJwtTokenException("Invalid or expired refresh token");
        }

        // Extract username from refresh token
            username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new InvalidJwtTokenException("Refresh token missing subject");
        }

        // Load user
        UserPrincipal userPrincipal;
        try {
            userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw new InvalidJwtTokenException("User not found for refresh token");
        }

        // Issue new tokens
        String newAccessToken = jwtService.generateJwtToken(userPrincipal);
        String newRefreshToken = jwtService.generateRefreshJwtToken(userPrincipal);

        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer", jwtExpiration);
    }
}