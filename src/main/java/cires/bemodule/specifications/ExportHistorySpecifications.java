package cires.bemodule.specifications;

import cires.bemodule.entities.ExportHistory;
import cires.bemodule.enums.ExportFormat;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

/**
 * Utility class providing {@link Specification} factories for
 * querying {@link ExportHistory} entities with dynamic filters.
 */
public class ExportHistorySpecifications {

    private ExportHistorySpecifications() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Filters by username (case‑insensitive, partial match).
     */
    public static Specification<ExportHistory> hasUsername(String username) {
        return (root, query, cb) -> {
            if (username == null || username.isBlank()) return null;
            return cb.like(cb.lower(root.get("exportedBy")), "%" + username.toLowerCase() + "%");
        };
    }

    /**
     * Filters by exact entity type.
     */
    public static Specification<ExportHistory> hasEntityType(String entityType) {
        return (root, query, cb) -> {
            if (entityType == null || entityType.isBlank()) return null;
            return cb.equal(root.get("entityType"), entityType);
        };
    }

    /**
     * Filters by export format.
     */
    public static Specification<ExportHistory> hasFormat(ExportFormat format) {
        return (root, query, cb) -> {
            if (format == null) return null;
            return cb.equal(root.get("exportFormat"), format);
        };
    }

    /**
     * Filters by minimum export date/time.
     */
    public static Specification<ExportHistory> exportedAfter(LocalDateTime from) {
        return (root, query, cb) -> {
            if (from == null) return null;
            return cb.greaterThanOrEqualTo(root.get("exportedAt"), from);
        };
    }

    /**
     * Filters by maximum export date/time.
     */
    public static Specification<ExportHistory> exportedBefore(LocalDateTime to) {
        return (root, query, cb) -> {
            if (to == null) return null;
            return cb.lessThanOrEqualTo(root.get("exportedAt"), to);
        };
    }
}