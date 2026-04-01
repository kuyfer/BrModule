package cires.bemodule.security.jwt;

import cires.bemodule.entities.Permission;
import cires.bemodule.models.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

//    public String getUserNameFromJwtToken(String token) {
//        return Jwts.parser().setSigningKey(key()).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List) {
            return ((List<?>) rolesObj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<String> extractPermissions(String token) {
        Claims claims = extractAllClaims(token);
        Object permsObj = claims.get("permissions");
        if (permsObj instanceof List) {
            return ((List<?>) permsObj).stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String generateToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }
    public String generateToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = userPrincipal.getUser().getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());
        claims.put("roles", roles);

        Set<String> permissions = userPrincipal.getUser().getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
        claims.put("permissions", permissions);

        return buildToken(claims, userPrincipal, jwtExpiration);
    }


    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    public String generateRefreshToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userPrincipal.getUser().getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return buildToken(claims, userPrincipal, refreshExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        logger.debug("Building token for user: {}, expiration: {}ms", userDetails.getUsername(), expiration);
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }
    private String buildToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        logger.debug("Building token for user: {}, expiration: {}ms", userPrincipal.getUsername(), jwtExpiration);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired for user: {}", userDetails.getUsername());
            return false;
        } catch (JwtException e) {
            logger.warn("Invalid token for user: {} - {}", userDetails.getUsername(), e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
// TODO: use Key instead or another class type
    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }
}

//package cires.bemodule.services;
//
//import io.jsonwebtoken.*;
//        import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.*;
//        import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Service
//public class JwtService {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
//
//    @Value("${jwt.secret}")
//    private String secretKey;
//
//    @Value("${jwt.expiration}")
//    private long jwtExpiration;
//
//    @Value("${jwt.refresh-expiration}")
//    private long refreshExpiration;
//
//    // --- Extract standard claims ---
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        return claimsResolver.apply(extractAllClaims(token));
//    }
//
//    // --- Extract roles and permissions (new) ---
//    public List<String> extractRoles(String token) {
//        Claims claims = extractAllClaims(token);
//        Object rolesObj = claims.get("roles");
//        if (rolesObj instanceof List) {
//            // JJWT returns a List of Object; we can cast each to String
//            return ((List<?>) rolesObj).stream()
//                    .map(Object::toString)
//                    .collect(Collectors.toList());
//        }
//        return Collections.emptyList();
//    }
//
//    public List<String> extractPermissions(String token) {
//        Claims claims = extractAllClaims(token);
//        Object permsObj = claims.get("permissions");
//        if (permsObj instanceof List) {
//            return ((List<?>) permsObj).stream()
//                    .map(Object::toString)
//                    .collect(Collectors.toList());
//        }
//        return Collections.emptyList();
//    }
//
//    // --- Token generation for standard UserDetails (no roles/permissions) ---
//    public String generateToken(UserDetails userDetails) {
//        return buildToken(new HashMap<>(), userDetails, jwtExpiration);
//    }
//
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return buildToken(extraClaims, userDetails, jwtExpiration);
//    }
//
//    public String generateRefreshToken(UserDetails userDetails) {
//        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
//    }
//
//    // --- Token generation for custom UserDetailsImpl (with roles/permissions) ---
//    public String generateToken(UserDetailsImpl userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//
//        // Extract roles
//        List<String> roles = userDetails.getUser().getRoles().stream()
//                .map(role -> role.getName().name()) // assuming ERole enum
//                .collect(Collectors.toList());
//        claims.put("roles", roles);
//
//        // Extract permissions
//        Set<String> permissions = userDetails.getUser().getRoles().stream()
//                .flatMap(role -> role.getPermissions().stream())
//                .map(Permission::getName)
//                .collect(Collectors.toSet());
//        claims.put("permissions", permissions);
//
//        return buildToken(claims, userDetails, jwtExpiration);
//    }
//
//    // Overload for refresh token if needed
//    public String generateRefreshToken(UserDetailsImpl userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        // Optionally include roles/permissions in refresh token as well
//        List<String> roles = userDetails.getUser().getRoles().stream()
//                .map(role -> role.getName().name())
//                .collect(Collectors.toList());
//        claims.put("roles", roles);
//        // ... permissions similarly
//        return buildToken(claims, userDetails, refreshExpiration);
//    }
//
//    // Core builder
//    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
//        logger.debug("Building token for user: {}, expiration: {}ms", userDetails.getUsername(), expiration);
//        return Jwts.builder()
//                .claims(extraClaims)
//                .subject(userDetails.getUsername())
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(getSignInKey(), Jwts.SIG.HS256)
//                .compact();
//    }
//
//    // --- Validation ---
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        try {
//            final String username = extractUsername(token);
//            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//        } catch (ExpiredJwtException e) {
//            logger.warn("Token expired for user: {}", userDetails.getUsername());
//            return false;
//        } catch (JwtException e) {
//            logger.warn("Invalid token for user: {} - {}", userDetails.getUsername(), e.getMessage());
//            return false;
//        }
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractClaim(token, Claims::getExpiration).before(new Date());
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(getSignInKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
//    private SecretKey getSignInKey() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
//    }
//}