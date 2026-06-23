package cires.bemodule.repositories;

import cires.bemodule.dtos.SessionParticipantLink;
import cires.bemodule.dtos.ValidatedImportRow;

import java.util.List;

public interface ParticipantBulkRepository {

    int[] bulkInsertParticipants(List<ValidatedImportRow> rows);
    int[] bulkInsertSessionLinks(List<SessionParticipantLink> links);
}
