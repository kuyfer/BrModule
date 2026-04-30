package cires.bemodule.dtos2;

import cires.bemodule.entities.Participant;
import cires.bemodule.enums.BulkActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerBulkRequest {
    private BulkActionType bulkActionType;
    private List<Participant> participants;

}
