package cires.bemodule.specifications;

import cires.bemodule.entities.Participant;
import cires.bemodule.entities.SessionParticipant;
import cires.bemodule.enums.RegistrationSource;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class providing static {@link Specification} factories for
 * querying {@link Participant} entities with dynamic filters.
 * <p>
 * These specifications are intended to be used with
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 * to build type‑safe, composable queries without writing JPQL.
 * </p>
 * <p>
 * The class is not meant to be instantiated – it contains only static helpers.
 * </p>
 *
 * @see Participant
 * @see RegistrationSource
 */
public class ParticipantsSpecifications {

    /**
     * Private constructor that prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException if called (including via reflection)
     */
    private ParticipantsSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a {@link Specification} that filters participants by their
     * {@link RegistrationSource}.
     * <p>
     * If the provided {@code source} is {@code null}, the specification becomes
     * a no‑op (a conjunction that matches all participants).
     * </p>
     *
     * @param source the registration source to filter on (e.g.,
     *               {@link RegistrationSource#MANUAL}); may be {@code null}
     *               to disable filtering
     * @return a specification that matches participants with the given
     *         registration source
     */
    public static Specification<Participant> hasRegistration(RegistrationSource source) {
        return (root, query, criteriaBuilder) ->
                source == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("registrationSource"), source);
    }

    public static Specification<Participant> hasSession(Long sessionId) {
        return (root, query, cb) -> {
            if (sessionId == null) return cb.conjunction();
            Join<Participant, SessionParticipant> spJoin = root.join("sessionParticipants");
            return cb.equal(spJoin.get("trainingSession").get("id"), sessionId);
        };
    }
}