package cires.bemodule.dtos.responses;

import cires.bemodule.entities.Participant;
import cires.bemodule.enums.BulkActionType;
import cires.bemodule.enums.BulkStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class CustomerBulkResponse {
    private BulkActionType bulkActionType;
    private List<Participant> participants;
    private BulkStatus status;

}
