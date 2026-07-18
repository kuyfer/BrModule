package cires.bemodule.configs;

import cires.bemodule.security.models.UserPrincipal;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} that supplies the username of the
 * currently authenticated user as the auditor for JPA auditing.
 * <p>
 * This bean is referenced by {@code @EnableJpaAuditing(auditorAwareRef = "auditorAware")}
 * in {@link AuditConfig}.  When an entity is persisted or updated, Spring Data JPA
 * calls {@link #getCurrentAuditor()} to obtain the value that will be written to
 * fields annotated with {@code @CreatedBy} or {@code @LastModifiedBy}.
 * </p>
 * <p>
 * The auditor is extracted from Spring Security's {@link SecurityContextHolder}:
 * <ul>
 *   <li>If the authentication is present and the principal is a
 *       {@link UserPrincipal}, the principal's username is returned.</li>
 *   <li>If authentication is absent or the principal is of another type,
 *       {@link Optional#empty()} is returned (auditor fields will be left
 *       {@code null}).</li>
 * </ul>
 * </p>
 *
 * @see AuditConfig
 * @see org.springframework.data.domain.AuditorAware
 * @see org.springframework.data.annotation.CreatedBy
 * @see org.springframework.data.annotation.LastModifiedBy
 */
@Component("auditorAware") // bean name referenced in @EnableJpaAuditing
public class AuditorAwareImpl implements AuditorAware<String> {

    /**
     * Retrieves the current auditor (username) from the security context.
     *
     * @return an {@link Optional} containing the username, or
     *         {@link Optional#empty()} if no authenticated user is available
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return Optional.of(userPrincipal.getUser().getUsername());
        }
        return Optional.empty();
    }
}