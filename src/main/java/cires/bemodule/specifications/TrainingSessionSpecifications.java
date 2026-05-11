package cires.bemodule.specifications;

import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import org.springframework.data.jpa.domain.Specification;

public class TrainingSessionSpecifications {

    public static Specification<TrainingSession> hasMode(TrainingSessionMode mode){
        return (root, query, criteriaBuilder) -> {
            if(mode == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("mode"), mode);
        };
    }

    public static Specification<TrainingSession> hasStatus(TrainingSessionStatus status){
        return (root, query, criteriaBuilder) -> {
            if(status == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
