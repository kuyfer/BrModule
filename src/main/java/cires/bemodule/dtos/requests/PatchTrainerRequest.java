package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request to partially update a trainer.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatchTrainerRequest {

    @Size(min = 3, max = 50)
    private String speciality;
}