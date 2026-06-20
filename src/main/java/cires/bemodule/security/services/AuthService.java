package cires.bemodule.security.services;

import cires.bemodule.dtos.responses.AuthResponse;
import cires.bemodule.dtos.requests.LoginRequest;
import cires.bemodule.dtos.requests.RefreshTokenRequest;
import cires.bemodule.exceptions.securityexceptions.InvalidJwtTokenException;
import cires.bemodule.security.models.UserPrincipal;
import cires.bemodule.security.jwt.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private static final String TOKEN_TYPE_CLAIM = "token_type";
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
        } catch (Exception e) {
            log.error("Unexpected login error", e);
            throw new RuntimeException("Login failed due to internal error", e);
        }
    }
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        final String refreshToken = request.getRefreshToken();
        final String userEmail;

        try {
            userEmail = jwtService.extractUsername(refreshToken);
        } catch (JwtException e) {
            throw e;
        }

        if (userEmail == null) {
            throw new InvalidJwtTokenException("Refresh token missing subject");
        }

        // Validate token type
        if (!REFRESH_TOKEN_TYPE.equals(jwtService.extractTokenType(refreshToken))) {
            throw new InvalidJwtTokenException("Invalid token type – expected refresh token");
        }

        // Load user
        UserPrincipal userPrincipal;
        try {
            userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(userEmail);
        } catch (UsernameNotFoundException e) {
            throw new InvalidJwtTokenException("User not found for refresh token");
        }

        // Validate token (signature, expiration, etc.)
        if (!jwtService.validateJwtToken(refreshToken)) {
            throw new InvalidJwtTokenException("Invalid or expired refresh token");
        }

        // Issue new tokens
        String newAccessToken = jwtService.generateJwtToken(userPrincipal);
        String newRefreshToken = jwtService.generateRefreshJwtToken(userPrincipal);

        return new AuthResponse(newAccessToken, newRefreshToken, "Bearer", jwtExpiration);
    }
}
