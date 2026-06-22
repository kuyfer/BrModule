package cires.bemodule.specifications;

import cires.bemodule.entities.Attendance;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AttendanceSpecifications {

    private AttendanceSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    private static Specification<Attendance> equalTo(String field, Object value) {
        return (root, q, cb) -> {
            if (value == null) return null;
            // supports nested paths like "session.id"
            String[] parts = field.split("\\.");
            var path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) path = path.get(parts[i]);
            return cb.equal(path, value);
        };
    }

    private static Specification<Attendance> dateFrom(LocalDate startTime) {
        return (root, q, cb) ->
                startTime == null ? null : cb.greaterThanOrEqualTo(root.get("date"), startTime);
    }

    private static Specification<Attendance> dateTo(LocalDate date) {
        return (root, q, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("date"), date);
    }
}
