package cires.bemodule.dtos.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * View projection for organizations with their subsidiaries.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String name;
    private String address;
    private String contactEmail;
    private String phone;
    private Integer subsidiaryCount;
    private List<SubsidiarySummaryDTO> subsidiaries;
}