package cires.bemodule.specifications;

import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class NotificationSpecifications {

    public static Specification<Notification> hasType(NotificationType type){
        return (root, query, criteriaBuilder) -> {
            if(type == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("notificationType"), type);
        };
    }

    public static Specification<Notification> hasStatus(NotificationStatus status){
        return (root, query, criteriaBuilder) -> {
            if(status == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("notificationStatus"), status);
        };
    }

    public static Specification<Notification> toEmailEquals(String email) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(email)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("toEmail"), email);
        };
    }



















}
