package cires.bemodule.dtos.views;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * View projection for subsidiaries.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SubsidiaryDTO {
    private Long id;
    private String name;
    private String address;
    private String organizationName;
}