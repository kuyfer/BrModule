package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Internal row for user activity metrics.
 */
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class UserActivityRow {
    private Long   userId;
    private String email;
    private long   eventCount;
}