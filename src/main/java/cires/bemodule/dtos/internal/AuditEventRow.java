package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Internal row representing an audit event.
 */
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditEventRow {
    private String        userEmail;
    private String        action;
    private String        module;
    private String        entityType;
    private LocalDateTime createdAt;
}