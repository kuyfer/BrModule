package cires.bemodule.security.jwt;

import cires.bemodule.entities.Permission;
import cires.bemodule.security.models.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ROLES_CLAIM = "roles";
    private static final String PERMISSIONS_CLAIM = "permissions";

    @Value("${jwt.secret}")
    private String jwtKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private List<String> extractListClaim(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        Object value = claims.get(claimName);
        if (value instanceof List<?> list) {
            return list.stream()
                    .map(Object::toString)
                    .toList();
        }
        return Collections.emptyList();
    }

    public List<String> extractRoles(String token) {
        return extractListClaim(token, ROLES_CLAIM);
    }

    public List<String> extractPermissions(String token) {
        return extractListClaim(token, PERMISSIONS_CLAIM);
    }

    public String generateJwtToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(TOKEN_TYPE_CLAIM, "access");
        List<String> roles = userPrincipal.getUser().getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        claims.put(ROLES_CLAIM, roles);

        Set<String> permissions = userPrincipal.getUser().getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
        claims.put(PERMISSIONS_CLAIM, permissions);

        return buildJwtToken(claims, userPrincipal, jwtExpiration);
    }

    public String generateRefreshJwtToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_TYPE_CLAIM, "refresh");

        List<String> roles = userPrincipal.getUser().getRoles().stream()
                .map(role -> role.getRoleName().name())
                .toList();
        claims.put(ROLES_CLAIM, roles);
        return buildJwtToken(claims, userPrincipal, refreshExpiration);
    }

    private String buildJwtToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        log.debug("Building token for user: {}, expiration: {}ms", userDetails.getUsername(), expiration);
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }
}
