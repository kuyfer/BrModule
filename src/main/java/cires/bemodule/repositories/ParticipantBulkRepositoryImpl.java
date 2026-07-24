package cires.bemodule.repositories;

import cires.bemodule.dtos.imports.SessionParticipantLink;
import cires.bemodule.dtos.imports.ValidatedImportRow;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ParticipantBulkRepositoryImpl implements ParticipantBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public int[] bulkInsertParticipants(List<ValidatedImportRow> rows) {
        String sql = """
            INSERT INTO participants
                (first_name, last_name, email, phone_number, registration_source)
            VALUES (?, ?, ?, ?, 'IMPORT')
            ON CONFLICT (email) DO UPDATE
                SET first_name = EXCLUDED.first_name,
                    last_name  = EXCLUDED.last_name,
                    phone_number = EXCLUDED.phone_number
            """;

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ValidatedImportRow row = rows.get(i);
                ps.setString(1, row.getFirstName());
                ps.setString(2, row.getLastName());
                ps.setString(3, row.getEmail());
                ps.setString(4, row.getPhone());   // null is fine
            }
            @Override
            public int getBatchSize() { return rows.size(); }
        });
    }

    @Override
    @Transactional
    public int[] bulkInsertSessionLinks(List<SessionParticipantLink> links) {
        String sql = """
            INSERT INTO session_participants (training_session_id, participant_id)
            SELECT ?, p.id
            FROM participants p
            WHERE p.email = ?
            ON CONFLICT (training_session_id, participant_id) DO NOTHING
            """;

        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                SessionParticipantLink link = links.get(i);
                ps.setLong(1, link.getSessionId());
                ps.setString(2, link.getEmail());
            }
            @Override
            public int getBatchSize() { return links.size(); }
        });
    }
}