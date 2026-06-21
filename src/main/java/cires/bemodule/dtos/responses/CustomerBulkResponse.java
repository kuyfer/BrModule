package cires.bemodule.dtos.responses;

import cires.bemodule.entities.Participant;
import cires.bemodule.enums.BulkActionType;
import cires.bemodule.enums.BulkStatus;
import lombok.*;

import java.util.List;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class CustomerBulkResponse {
    private BulkActionType bulkActionType;
    private List<Participant> participants;
    private BulkStatus status;

}
