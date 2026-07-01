package cires.bemodule.dtos.views;

import lombok.*;

/**
 * Summary view for subsidiaries (used inside organization views).
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class SubsidiarySummaryDTO {
    private Long id;
    private String name;
}