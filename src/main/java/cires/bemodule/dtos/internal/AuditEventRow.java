package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class AuditEventRow {
    private String        userEmail;
    private String        action;
    private String        module;
    private String        entityType;
    private LocalDateTime createdAt;
}