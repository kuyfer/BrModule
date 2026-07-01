package cires.bemodule.repositories;

import cires.bemodule.dtos.imports.SessionParticipantLink;
import cires.bemodule.dtos.imports.ValidatedImportRow;

import java.util.List;

public interface ParticipantBulkRepository {

    int[] bulkInsertParticipants(List<ValidatedImportRow> rows);
    int[] bulkInsertSessionLinks(List<SessionParticipantLink> links);
}
