package cires.bemodule.specifications;

import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class NotificationSpecifications {

    private NotificationSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<Notification> hasType(NotificationType type) {
        return (root, query, criteriaBuilder) ->
                type == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("notificationType"), type);
    }

    public static Specification<Notification> hasStatus(NotificationStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("notificationStatus"), status);
    }

    public static Specification<Notification> toEmailEquals(String email) {
        return (root, query, criteriaBuilder) ->
                !StringUtils.hasText(email) ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("toEmail"), email);
    }
}