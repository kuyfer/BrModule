-- V3: Extend attendance and its audit table to match the updated entity

-- 1. Change start_time type from TIMESTAMP to DATE
ALTER TABLE attendance
ALTER COLUMN start_time TYPE DATE;
ALTER TABLE attendance_aud
ALTER COLUMN start_time TYPE DATE;

-- 2. Add new business columns to the main table
ALTER TABLE attendance
    ADD COLUMN period              VARCHAR(255),
    ADD COLUMN session_title       VARCHAR(255),
    ADD COLUMN validated           BOOLEAN      NOT NULL DEFAULT false,
    ADD COLUMN validated_by        BIGINT,
    ADD COLUMN validated_at        TIMESTAMP WITHOUT TIME ZONE,
    ADD COLUMN slot                VARCHAR(255),
    ADD COLUMN status              VARCHAR(30)  NOT NULL DEFAULT 'PRESENT',  -- adjust default to match your enum

    ADD COLUMN date                DATE,
    ADD COLUMN delay_reason        VARCHAR(255),
    ADD COLUMN comment             VARCHAR(255),
    ADD COLUMN correction_reason   VARCHAR(255),
    ADD COLUMN audit_note          TEXT;

-- 3. Mirror all new columns in the Envers audit table (always nullable)
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
    ADD COLUMN audit_note          TEXT;

-- 4. Add foreign keys for the new session & participant relationships
-- ALTER TABLE attendance
--     ADD CONSTRAINT fk_attendance_session
--         FOREIGN KEY (session_id) REFERENCES session(id);
--
-- ALTER TABLE attendance
--     ADD CONSTRAINT fk_attendance_participant
--         FOREIGN KEY (participant_id) REFERENCES participants(id);
--
