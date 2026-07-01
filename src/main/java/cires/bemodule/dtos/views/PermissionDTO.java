package cires.bemodule.dtos.views;

import lombok.*;

/**
 * View projection for permissions.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class PermissionDTO {
    private Long id;
    private String name;
    private String resource;
    private String action;
}