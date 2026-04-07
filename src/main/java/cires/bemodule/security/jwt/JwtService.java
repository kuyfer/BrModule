package cires.bemodule.security.jwt;

import cires.bemodule.entities.Permission;
import cires.bemodule.models.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<String> extractRoles(String token) {
        return extractListClaim(token, "roles");
    }

    public List<String> extractPermissions(String token) {
        return extractListClaim(token, "permissions");
    }

    public String generateJwtToken(UserPrincipal userPrincipal) {
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

        return buildJwtToken(claims, userPrincipal, jwtExpiration);
    }

    public String generateRefreshJwtToken(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userPrincipal.getUser().getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toList());
        claims.put("roles", roles);
        return buildJwtToken(claims, userPrincipal, refreshExpiration);
    }

    private String buildJwtToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        logger.debug("Building token for user: {}, expiration: {}ms", userDetails.getUsername(), expiration);
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
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

// TODO: use Key instead or another class type
    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
    }
}
