package cires.bemodule.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatchSubsidiaryRequest {
    private String name;
    private String address;
    private Long organizationId;
}
