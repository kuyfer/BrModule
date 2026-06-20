package cires.bemodule.dtos.requests;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CancelTrainingSessionRequest {

    private String reason;

}
