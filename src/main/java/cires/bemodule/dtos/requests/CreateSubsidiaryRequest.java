package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to create a new subsidiary (office/branch) under an organization.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateSubsidiaryRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String address;

    private Long organizationId;
}