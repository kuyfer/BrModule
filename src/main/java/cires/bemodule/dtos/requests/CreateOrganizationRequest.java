package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to create a new organization.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateOrganizationRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String address;

    @Email(message = "Invalid email format")
    private String contactEmail;

    private String phone;
}