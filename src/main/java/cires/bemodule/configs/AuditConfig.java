package cires.bemodule.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration that enables JPA Auditing for the application.
 * <p>
 * The {@link EnableJpaAuditing} annotation activates the auditing infrastructure,
 * using the provided {@code auditorAwareRef} bean to automatically populate
 * {@code @CreatedBy} and {@code @LastModifiedBy} fields on entity classes.
 * </p>
 *
 * @see org.springframework.data.domain.AuditorAware
 */
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration
public class AuditConfig {
}