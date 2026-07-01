package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to partially update an organization.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatchOrganizationRequest {
    private String name;
    private String address;
    @Email(message = "Invalid email format")
    private String contactEmail;
    private String phone;
}