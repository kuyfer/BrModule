-- ============================================================
-- V2: Remodel attendance table to match the final entity
--     (replaces multiple incremental scripts)
-- ============================================================

-- 1. Add all missing columns (nullable first to allow data migration)
ALTER TABLE attendance
    ADD COLUMN period              VARCHAR(255),
    ADD COLUMN session_title       VARCHAR(255),
    ADD COLUMN validated           BOOLEAN      NOT NULL DEFAULT false,
    ADD COLUMN validated_by        BIGINT,
    ADD COLUMN validated_at        TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN slot                VARCHAR(255),
    ADD COLUMN status              VARCHAR(30)  NOT NULL DEFAULT 'PRESENT',
    ADD COLUMN date                DATE,
    ADD COLUMN delay_reason        VARCHAR(255),
    ADD COLUMN comment             VARCHAR(255),
    ADD COLUMN correction_reason   VARCHAR(255),
    ADD COLUMN audit_note          TEXT,
    ADD COLUMN session_id          BIGINT,
    ADD COLUMN participant_id      BIGINT;

-- 2. Update the Envers audit table with the same columns (always nullable)
ALTER TABLE attendance_aud
    ADD COLUMN period              VARCHAR(255),
    ADD COLUMN session_title       VARCHAR(255),
    ADD COLUMN validated           BOOLEAN,
    ADD COLUMN validated_by        BIGINT,
    ADD COLUMN validated_at        TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN slot                VARCHAR(255),
    ADD COLUMN status              VARCHAR(30),
    ADD COLUMN date                DATE,
    ADD COLUMN delay_reason        VARCHAR(255),
    ADD COLUMN comment             VARCHAR(255),
    ADD COLUMN correction_reason   VARCHAR(255),
    ADD COLUMN audit_note          TEXT,
    ADD COLUMN session_id          BIGINT,
    ADD COLUMN participant_id      BIGINT;

-- 3. Migrate existing data BEFORE dropping old columns
--    (convert old attendance_status → status, start_time → date)
UPDATE attendance
SET status = attendance_status
WHERE attendance_status IS NOT NULL;

UPDATE attendance
SET date = start_time::date
WHERE start_time IS NOT NULL;

-- 4. Drop the legacy columns that are no longer used
ALTER TABLE attendance
    DROP COLUMN attendance_status,
    DROP COLUMN start_time,
    DROP COLUMN end_time;

-- 5. Drop legacy columns from the audit table
ALTER TABLE attendance_aud
    DROP COLUMN IF EXISTS attendance_status,
    DROP COLUMN IF EXISTS start_time,
    DROP COLUMN IF EXISTS end_time;

-- 6. Add foreign keys for the new session & participant relationships
ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_session
        FOREIGN KEY (session_id) REFERENCES session(id);

ALTER TABLE attendance
    ADD CONSTRAINT fk_attendance_participant
        FOREIGN KEY (participant_id) REFERENCES participants(id);

-- 7. Make the FK columns and date NOT NULL (the table is now ready for new data)
ALTER TABLE attendance
    ALTER COLUMN session_id SET NOT NULL,
    ALTER COLUMN participant_id SET NOT NULL,
    ALTER COLUMN date SET NOT NULL;