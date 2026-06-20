package cires.bemodule.dtos.requests;

import cires.bemodule.entities.Participant;
import cires.bemodule.enums.BulkActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CustomerBulkRequest {
    private BulkActionType bulkActionType;
    private List<Participant> participants;

}
