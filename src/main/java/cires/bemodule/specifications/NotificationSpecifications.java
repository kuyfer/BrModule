package cires.bemodule.specifications;

import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * A utility class that provides static {@link Specification} factories for
 * building type‑safe, dynamic queries on {@link Notification} entities.
 * <p>
 * These specifications are typically used with
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 * to filter notifications by type, status, or recipient email without
 * writing custom JPQL.
 * </p>
 * <p>
 * This class is not intended to be instantiated – it contains only static
 * helper methods.
 * </p>
 *
 * @see Notification
 * @see NotificationType
 * @see NotificationStatus
 */
public class NotificationSpecifications {

    /**
     * Private constructor to prevent instantiation of the utility class.
     *
     * @throws UnsupportedOperationException always
     */
    private NotificationSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a {@link Specification} that filters notifications by their
     * {@link NotificationType}.
     * <p>
     * If the given {@code type} is {@code null}, the specification becomes a
     * no‑op (a conjunction that matches all notifications).
     * </p>
     *
     * @param type the notification type to match (e.g.,
     *             {@link NotificationType#SESSION_REMINDER}); may be
     *             {@code null} to disable filtering
     * @return a specification for the requested notification type
     */
    public static Specification<Notification> hasType(NotificationType type) {
        return (root, query, criteriaBuilder) ->
                type == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("notificationType"), type);
    }

    /**
     * Creates a {@link Specification} that filters notifications by their
     * {@link NotificationStatus}.
     * <p>
     * When {@code status} is {@code null}, the filter is effectively ignored
     * and all notifications match.
     * </p>
     *
     * @param status the notification status to match (e.g.,
     *               {@link NotificationStatus#PENDING}); may be {@code null}
     *               to disable filtering
     * @return a specification for the given status
     */
    public static Specification<Notification> hasStatus(NotificationStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("notificationStatus"), status);
    }

    /**
     * Creates a {@link Specification} that filters notifications where the
     * recipient email address equals the provided value.
     * <p>
     * The filter is only applied when {@code email} is non‑blank (contains
     * text). If it is {@code null}, empty, or whitespace only, the
     * specification returns a conjunction that matches all notifications.
     * </p>
     *
     * @param email the exact email address to match; may be {@code null}
     *              or blank to disable filtering
     * @return a specification that restricts notifications to the given
     *         recipient email
     */
    public static Specification<Notification> toEmailEquals(String email) {
        return (root, query, criteriaBuilder) ->
                !StringUtils.hasText(email) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("toEmail"), email);
    }
}