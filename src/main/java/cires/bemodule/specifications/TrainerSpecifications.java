package cires.bemodule.specifications;

import cires.bemodule.entities.Trainer;
import org.springframework.data.jpa.domain.Specification;

public class TrainerSpecifications {

    private TrainerSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<Trainer> hasSpeciality(String speciality){
        return (root, query, criteriaBuilder) -> {
            if(speciality == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("speciality"), speciality);
        };
    }
}
