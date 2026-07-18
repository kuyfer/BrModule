package cires.bemodule.configs;

import cires.bemodule.security.jwt.JwtAuthenticationFilter;
import cires.bemodule.security.jwt.AuthEntryPointJwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Core Spring Security configuration for the application.
 * <p>
 * Defines the security filter chain, authentication provider, password
 * encoder, and JWT‑based stateless authentication.  Method‑level security
 * is enabled via {@link EnableMethodSecurity}.
 * </p>
 *
 * @see JwtAuthenticationFilter
 * @see AuthEntryPointJwt
 * @see CorsConfigurationSource
 */
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Configuration
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * Builds the {@link SecurityFilterChain} that governs access to the
     * application’s HTTP endpoints.
     * <p>
     * The chain is configured as follows:
     * <ul>
     *   <li>CORS is enabled using the provided {@link CorsConfigurationSource}.</li>
     *   <li>CSRF protection is disabled (suitable for stateless JWT APIs).</li>
     *   <li>Custom {@link AuthEntryPointJwt} handles 401 responses.</li>
     *   <li>XSS protection headers are set, and a minimal Content‑Security‑Policy
     *       is applied.</li>
     *   <li>Pre‑flight OPTIONS requests and specific public endpoints
     *       ({@code /api/auth/**}, {@code /actuator/**}, {@code /api/passwd/**})
     *       are permitted without authentication; all other requests require
     *       authentication.</li>
     *   <li>Session management is set to stateless.</li>
     *   <li>The {@link JwtAuthenticationFilter} is inserted before
     *       {@link UsernamePasswordAuthenticationFilter} to validate JWTs on
     *       every request.</li>
     * </ul>
     * </p>
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the built {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(unauthorizedHandler))

                .headers(headers -> headers
                        .xssProtection(xss ->
                                xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(cps ->
                                cps.policyDirectives("script-src 'self'"))
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/passwd/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides a {@link DaoAuthenticationProvider} that uses the custom
     * {@link UserDetailsService} and the configured {@link PasswordEncoder}.
     *
     * @return the authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes the {@link AuthenticationManager} as a bean for use in other
     * components (e.g., authentication controller).
     *
     * @param config the authentication configuration
     * @return the authentication manager
     * @throws AuthenticationException if the manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws AuthenticationException {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the password encoder bean (BCrypt, version $2B, strength 12).
     *
     * @return a {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 12);
    }
}