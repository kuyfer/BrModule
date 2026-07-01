package cires.bemodule.dtos.requests;

import lombok.*;

/**
 * Request to create a new trainer linked to an existing user.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateTrainerRequest {
    private String speciality;
}