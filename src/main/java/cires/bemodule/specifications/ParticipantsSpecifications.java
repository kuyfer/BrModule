package cires.bemodule.specifications;

import cires.bemodule.entities.Participant;
import cires.bemodule.enums.RegistrationSource;
import org.springframework.data.jpa.domain.Specification;

public class ParticipantsSpecifications {

    private ParticipantsSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<Participant> hasRegistration(RegistrationSource source){
        return (root, query, criteriaBuilder) -> {
            if(source == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("registrationSource"), source);
        };
    }
}
