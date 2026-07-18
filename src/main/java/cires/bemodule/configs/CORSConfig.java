package cires.bemodule.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration class that defines Cross‑Origin Resource Sharing (CORS) rules
 * for the application.
 * <p>
 * The bean {@code corsConfigurationSource} is automatically picked up by
 * Spring Security when the DSL {@code .cors(cors -> cors.configurationSource(...))}
 * is used in the security filter chain.  It allows only the frontend
 * development server ({@code http://localhost:5173}) to make requests to the
 * {@code /api/**} endpoints.
 * </p>
 *
 * @see cires.bemodule.configs.WebSecurityConfig
 */
@Configuration
public class CORSConfig {

    /**
     * Creates the CORS configuration source that applies to all API endpoints.
     * <p>
     * The configuration:
     * <ul>
     *   <li>Allows origin {@code http://localhost:5173}.</li>
     *   <li>Permits the HTTP methods GET, POST, PUT, PATCH, DELETE, and OPTIONS.</li>
     *   <li>Allows all headers ({@code *}).</li>
     *   <li>Exposes the {@code Authorization} header so the client can read the
     *       token from responses.</li>
     *   <li>Does not require credentials (cookies, etc.).</li>
     *   <li>Caches the pre‑flight response for 1 hour (3600 seconds).</li>
     * </ul>
     * </p>
     *
     * @return a {@link CorsConfigurationSource} mapped to {@code /api/**}
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }
}