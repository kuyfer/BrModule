package cires.bemodule.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        logger.debug("Extracting username from JWT token");
        try {
            String username = extractClaim(token, Claims::getSubject);
            logger.debug("Successfully extracted username: {}", username);
            return username;
        } catch (JwtException e) {
            logger.warn("Failed to extract username from token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error extracting username from token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to extract username", e);
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.debug("Extracting claim from JWT token");
        try {
            final Claims claims = extractAllClaims(token);
            T claim = claimsResolver.apply(claims);
            logger.debug("Successfully extracted claim from token");
            return claim;
        } catch (JwtException e) {
            logger.warn("Failed to extract claim from token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error extracting claim from token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to extract claim", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Generating JWT access token for user: {}", userDetails.getUsername());
        try {
            String token = generateToken(new HashMap<>(), userDetails);
            logger.info("Successfully generated JWT access token for user: {}", userDetails.getUsername());
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate JWT access token for user: {} - {}", 
                        userDetails.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Failed to generate access token", e);
        }
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        logger.info("Generating JWT access token with extra claims for user: {}", userDetails.getUsername());
        logger.debug("Extra claims count: {}", extraClaims.size());
        try {
            String token = buildToken(extraClaims, userDetails, jwtExpiration);
            logger.info("Successfully generated JWT access token with extra claims for user: {}", 
                       userDetails.getUsername());
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate JWT access token with extra claims for user: {} - {}", 
                        userDetails.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Failed to generate access token with extra claims", e);
        }
    }

    public String generateRefreshToken(UserDetails userDetails) {
        logger.info("Generating JWT refresh token for user: {}", userDetails.getUsername());
        try {
            String token = buildToken(new HashMap<>(), userDetails, refreshExpiration);
            logger.info("Successfully generated JWT refresh token for user: {}", userDetails.getUsername());
            return token;
        } catch (Exception e) {
            logger.error("Failed to generate JWT refresh token for user: {} - {}", 
                        userDetails.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        logger.debug("Building JWT token for user: {} with expiration: {}ms", 
                    userDetails.getUsername(), expiration);
        
        try {
            Date issuedAt = new Date(System.currentTimeMillis());
            Date expirationDate = new Date(System.currentTimeMillis() + expiration);
            
            logger.debug("Token validity period: {} to {}", issuedAt, expirationDate);
            
            String token = Jwts
                    .builder()
                    .claims(extraClaims)
                    .subject(userDetails.getUsername())
                    .issuedAt(issuedAt)
                    .expiration(expirationDate)
                    .signWith(getSignInKey(), Jwts.SIG.HS256)
                    .compact();
                    
            logger.debug("Successfully built JWT token for user: {}", userDetails.getUsername());
            return token;
            
        } catch (Exception e) {
            logger.error("Failed to build JWT token for user: {} - {}", 
                        userDetails.getUsername(), e.getMessage(), e);
            throw new RuntimeException("Failed to build JWT token", e);
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.debug("Validating JWT token for user: {}", userDetails.getUsername());
        
        try {
            final String tokenUsername = extractUsername(token);
            boolean usernameMatches = tokenUsername.equals(userDetails.getUsername());
            boolean isNotExpired = !isTokenExpired(token);
            boolean isValid = usernameMatches && isNotExpired;
            
            if (!usernameMatches) {
                logger.warn("Token validation failed - username mismatch. Expected: {}, Found: {}", 
                           userDetails.getUsername(), tokenUsername);
            }
            
            if (!isNotExpired) {
                logger.warn("Token validation failed - token expired for user: {}", userDetails.getUsername());
            }
            
            if (isValid) {
                logger.debug("JWT token validation successful for user: {}", userDetails.getUsername());
            } else {
                logger.warn("JWT token validation failed for user: {}", userDetails.getUsername());
            }
            
            return isValid;
            
        } catch (ExpiredJwtException e) {
            logger.warn("Token validation failed - token expired for user: {} at {}", 
                       userDetails.getUsername(), e.getClaims().getExpiration());
            return false;
        } catch (SignatureException e) {
            logger.warn("Token validation failed - invalid signature for user: {}", userDetails.getUsername());
            return false;
        } catch (MalformedJwtException e) {
            logger.warn("Token validation failed - malformed token for user: {}", userDetails.getUsername());
            return false;
        } catch (JwtException e) {
            logger.warn("Token validation failed for user: {} - {}", userDetails.getUsername(), e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during token validation for user: {} - {}", 
                        userDetails.getUsername(), e.getMessage(), e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        logger.debug("Checking if JWT token is expired");
        try {
            Date expiration = extractExpiration(token);
            Date now = new Date();
            boolean expired = expiration.before(now);
            
            logger.debug("Token expiration check - Expires: {}, Current: {}, Expired: {}", 
                        expiration, now, expired);
            
            return expired;
        } catch (ExpiredJwtException e) {
            logger.debug("Token is expired: {}", e.getClaims().getExpiration());
            return true;
        } catch (JwtException e) {
            logger.warn("Failed to check token expiration: {}", e.getMessage());
            throw e;
        }
    }

    private Date extractExpiration(String token) {
        logger.debug("Extracting expiration date from JWT token");
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            logger.debug("Successfully extracted token expiration: {}", expiration);
            return expiration;
        } catch (JwtException e) {
            logger.warn("Failed to extract expiration from token: {}", e.getMessage());
            throw e;
        }
    }

    private Claims extractAllClaims(String token) {
        logger.debug("Extracting all claims from JWT token");
        try {
            Claims claims = Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
                    
            logger.debug("Successfully extracted all claims from token. Subject: {}, Expiration: {}", 
                        claims.getSubject(), claims.getExpiration());
            return claims;
            
        } catch (ExpiredJwtException e) {
            logger.debug("Token expired but claims extracted. Subject: {}, Expiration: {}", 
                        e.getClaims().getSubject(), e.getClaims().getExpiration());
            throw e;
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token: {}", e.getMessage());
            throw e;
        } catch (JwtException e) {
            logger.warn("JWT processing error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error extracting claims from token: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to extract claims", e);
        }
    }

    private SecretKey getSignInKey() {
        logger.debug("Generating signing key from secret");
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            logger.debug("Successfully generated signing key");
            return key;
        } catch (Exception e) {
            logger.error("Failed to generate signing key: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate signing key", e);
        }
    }
}
