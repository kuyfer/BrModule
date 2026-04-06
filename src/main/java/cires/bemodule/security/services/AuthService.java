package cires.bemodule.security.services;

import cires.bemodule.dtos.AuthResponse;
import cires.bemodule.dtos.LoginRequest;
import cires.bemodule.dtos.RefreshTokenRequest;
import cires.bemodule.models.UserPrincipal;
import cires.bemodule.security.jwt.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserDetailsService userDetailsService, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthResponse login(LoginRequest request) {
        
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var userPrincipal = userDetailsService.loadUserByUsername(request.getUsername());
            var jwtToken = jwtService.generateToken((UserPrincipal) userPrincipal);
            var refreshToken = jwtService.generateRefreshToken((UserPrincipal) userPrincipal);

            return new AuthResponse(jwtToken, refreshToken, "Bearer", jwtExpiration);
            
        } catch (BadCredentialsException e) {
            logger.warn("Login failed - Invalid credentials for username: {}", request.getUsername());
            throw e;
        } catch (UsernameNotFoundException e) {
            logger.warn("Login failed - User not found: {}", request.getUsername());
            throw e;
        } catch (AuthenticationException e) {
            logger.error("Authentication error for username: {} - {}", request.getUsername(), e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during login for username: {} - {}", 
                        request.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Login failed due to internal error", e);
        }
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        logger.info("Token refresh attempt initiated");
        
        try {
            final String refreshToken = request.getRefreshToken();
            logger.debug("Extracting username from refresh token");
            
            final String userEmail = jwtService.extractUsername(refreshToken);
            
            if (userEmail == null) {
                logger.warn("Token refresh failed - Unable to extract username from token");
                throw new RuntimeException("Invalid refresh token - no username found");
            }
            
            logger.debug("Token refresh requested for user: {}", userEmail);
            
            // Load user details
            logger.debug("Loading user details for token refresh: {}", userEmail);
            var userPrincipal = this.userDetailsService.loadUserByUsername(userEmail);
            
            // Validate refresh token
            logger.debug("Validating refresh token for user: {}", userEmail);
            if (jwtService.validateJwtToken(refreshToken)) {
                logger.debug("Refresh token valid, generating new tokens for user: {}", userEmail);
                
                var accessToken = jwtService.generateToken((UserPrincipal) userPrincipal);
                var newRefreshToken = jwtService.generateRefreshToken((UserPrincipal) userPrincipal);
                
                logger.info("Token refresh completed successfully for user: {}", userEmail);
                return new AuthResponse(accessToken, newRefreshToken, "Bearer", jwtExpiration);
            } else {
                logger.warn("Token refresh failed - Invalid or expired refresh token for user: {}", userEmail);
                throw new RuntimeException("Invalid or expired refresh token");
            }
            
        } catch (UsernameNotFoundException e) {
            logger.warn("Token refresh failed - User not found during refresh token validation");
            throw new RuntimeException("Invalid refresh token - user not found", e);
        } catch (Exception e) {
            logger.error("Unexpected error during token refresh: {}", e.getMessage(), e);
            throw new RuntimeException("Token refresh failed due to internal error", e);
        }
    }
}
