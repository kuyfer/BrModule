//package cires.bemodule.services;
//
//import com.yourapp.core.exception.BusinessException;
//import com.yourapp.core.exception.ResourceNotFoundException;
//import com.yourapp.modules.entities.repository.EntityRepository;
//import com.yourapp.modules.participants.dto.*;
//import com.yourapp.modules.participants.entity.Participant;
//import com.yourapp.modules.participants.mapper.ParticipantMapper;
//import com.yourapp.modules.participants.repository.ParticipantRepository;
//import com.yourapp.modules.participants.service.ParticipantService;
//import com.yourapp.modules.sessions.repository.SessionParticipantRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVParser;
//import org.apache.commons.csv.CSVRecord;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ParticipantServiceImpl {
//
//    private static final List<String> SUPPORTED_IMPORT_CONTENT_TYPES = List.of(
//            "text/csv", "application/vnd.ms-excel", "application/octet-stream"
//    );
//
//    private final ParticipantRepository participantRepository;
//    private final EntityRepository entityRepository;
//    private final SessionParticipantRepository sessionParticipantRepository;
//    private final ParticipantMapper participantMapper;
//
//    // ─── CREATE ──────────────────────────────────────────────────────────────
//
//    @Override
//    public ParticipantResponse createParticipant(CreateParticipantRequest request, UUID createdByUserId) {
//        assertEntityExists(request.getEntityId());
//
//        // Email uniqueness scoped per entity
//        if (participantRepository.existsByEmailAndEntityId(request.getEmail(), request.getEntityId())) {
//            throw new BusinessException("A participant with this email already exists in the given entity.");
//        }
//
//        Participant participant = participantMapper.toEntity(request);
//        participant.setArchived(false);
//        participant.setCreatedBy(createdByUserId);
//        participant.setUpdatedBy(createdByUserId);
//
//        Participant saved = participantRepository.save(participant);
//        log.info("Participant created [id={}, email={}, by={}]", saved.getId(), saved.getEmail(), createdByUserId);
//        return participantMapper.toResponse(saved);
//    }
//
//    // ─── UPDATE ──────────────────────────────────────────────────────────────
//
//    @Override
//    public ParticipantResponse updateParticipant(UUID participantId, UpdateParticipantRequest request, UUID updatedByUserId) {
//        Participant participant = findParticipantOrThrow(participantId);
//
//        // If email is being changed, verify it is still unique
//        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(participant.getEmail())) {
//            if (participantRepository.existsByEmailAndEntityIdAndIdNot(
//                    request.getEmail(), participant.getEntityId(), participantId)) {
//                throw new BusinessException("Another participant with this email already exists in the entity.");
//            }
//        }
//
//        participantMapper.updateEntityFromRequest(request, participant);
//        participant.setUpdatedBy(updatedByUserId);
//
//        Participant saved = participantRepository.save(participant);
//        log.info("Participant updated [id={}, by={}]", saved.getId(), updatedByUserId);
//        return participantMapper.toResponse(saved);
//    }
//
//    // ─── READ ─────────────────────────────────────────────────────────────────
//
//    @Override
//    @Transactional(readOnly = true)
//    public ParticipantResponse getParticipantById(UUID participantId) {
//        return participantMapper.toResponse(findParticipantOrThrow(participantId));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public Page<ParticipantSummary> searchParticipants(ParticipantFilterRequest filter, Pageable pageable) {
//        return participantRepository.searchParticipants(
//                filter.getEntityId(),
//                filter.getSubsidiaryId(),
//                filter.isArchivedOnly(),
//                filter.getSearch(),
//                pageable
//        ).map(participantMapper::toSummary);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ParticipantSessionHistory> getSessionHistoryForParticipant(UUID participantId) {
//        findParticipantOrThrow(participantId); // existence check
//        return sessionParticipantRepository.findSessionHistoryByParticipantId(participantId);
//    }
//
//    // ─── BULK IMPORT ─────────────────────────────────────────────────────────
//
//    /**
//     * Imports participants from a CSV file.
//     * Expected headers: firstName, lastName, email, jobTitle, subsidiaryId (optional)
//     * Rows with validation errors are skipped and reported back.
//     * Successfully parsed rows are upserted (insert or update by email+entity).
//     */
//    @Override
//    public BulkImportResult importParticipantsFromFile(MultipartFile file, UUID entityId, UUID importedByUserId) {
//        assertEntityExists(entityId);
//
//        if (file == null || file.isEmpty()) {
//            throw new BusinessException("Import file is empty.");
//        }
//        if (!SUPPORTED_IMPORT_CONTENT_TYPES.contains(file.getContentType())) {
//            throw new BusinessException("Unsupported file type. Please upload a CSV file.");
//        }
//
//        List<String> errors = new ArrayList<>();
//        List<Participant> toSave = new ArrayList<>();
//        int rowNumber = 1; // starts at 1 (row 0 = header)
//
//        try (BufferedReader reader = new BufferedReader(
//                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
//             CSVParser parser = CSVFormat.DEFAULT
//                     .withFirstRecordAsHeader()
//                     .withIgnoreHeaderCase()
//                     .withTrim()
//                     .parse(reader)) {
//
//            for (CSVRecord record : parser) {
//                rowNumber++;
//                try {
//                    String firstName = requireField(record, "firstName", rowNumber);
//                    String lastName  = requireField(record, "lastName",  rowNumber);
//                    String email     = requireField(record, "email",     rowNumber);
//
//                    if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
//                        errors.add(String.format("Row %d: invalid email format '%s'.", rowNumber, email));
//                        continue;
//                    }
//
//                    // Upsert: reuse existing profile if the email already belongs to this entity
//                    Participant participant = participantRepository
//                            .findByEmailAndEntityId(email, entityId)
//                            .orElseGet(Participant::new);
//
//                    participant.setFirstName(firstName);
//                    participant.setLastName(lastName);
//                    participant.setEmail(email.toLowerCase());
//                    participant.setJobTitle(record.isMapped("jobTitle") ? record.get("jobTitle") : null);
//                    participant.setEntityId(entityId);
//                    participant.setArchived(false);
//
//                    if (participant.getId() == null) {
//                        participant.setCreatedBy(importedByUserId);
//                    }
//                    participant.setUpdatedBy(importedByUserId);
//
//                    toSave.add(participant);
//
//                } catch (BusinessException e) {
//                    errors.add(e.getMessage());
//                }
//            }
//
//        } catch (Exception e) {
//            throw new BusinessException("Failed to parse import file: " + e.getMessage());
//        }
//
//        List<Participant> saved = participantRepository.saveAll(toSave);
//        log.info("Bulk import: {} participants saved, {} errors — entity={}, by={}",
//                saved.size(), errors.size(), entityId, importedByUserId);
//
//        return BulkImportResult.builder()
//                .totalRows(rowNumber - 1)
//                .importedCount(saved.size())
//                .errorCount(errors.size())
//                .errors(errors)
//                .build();
//    }
//
//    // ─── ARCHIVE / RESTORE ────────────────────────────────────────────────────
//
//    @Override
//    public void archiveParticipant(UUID participantId, UUID updatedByUserId) {
//        Participant participant = findParticipantOrThrow(participantId);
//
//        if (participant.isArchived()) {
//            throw new BusinessException("Participant is already archived.");
//        }
//
//        participant.setArchived(true);
//        participant.setUpdatedBy(updatedByUserId);
//        participantRepository.save(participant);
//        log.info("Participant archived [id={}, by={}]", participantId, updatedByUserId);
//    }
//
//    @Override
//    public void restoreParticipant(UUID participantId, UUID updatedByUserId) {
//        Participant participant = findParticipantOrThrow(participantId);
//
//        if (!participant.isArchived()) {
//            throw new BusinessException("Participant is not archived.");
//        }
//
//        participant.setArchived(false);
//        participant.setUpdatedBy(updatedByUserId);
//        participantRepository.save(participant);
//        log.info("Participant restored [id={}, by={}]", participantId, updatedByUserId);
//    }
//
//    // ─── DELETE ───────────────────────────────────────────────────────────────
//
//    @Override
//    public void deleteParticipant(UUID participantId, UUID deletedByUserId) {
//        Participant participant = findParticipantOrThrow(participantId);
//
//        boolean hasSessionHistory = sessionParticipantRepository.existsByParticipantId(participantId);
//        if (hasSessionHistory) {
//            throw new BusinessException(
//                    "Cannot delete a participant with session history. Archive them instead."
//            );
//        }
//
//        participant.setDeletedAt(LocalDateTime.now());
//        participant.setUpdatedBy(deletedByUserId);
//        participantRepository.save(participant);
//        log.warn("Participant soft-deleted [id={}, by={}]", participantId, deletedByUserId);
//    }
//
//    // ─── PRIVATE HELPERS ──────────────────────────────────────────────────────
//
//    private Participant findParticipantOrThrow(UUID participantId) {
//        return participantRepository.findByIdAndDeletedAtIsNull(participantId)
//                .orElseThrow(() -> new ResourceNotFoundException("Participant not found: " + participantId));
//    }
//
//    private void assertEntityExists(UUID entityId) {
//        if (!entityRepository.existsById(entityId)) {
//            throw new ResourceNotFoundException("Entity not found: " + entityId);
//        }
//    }
//
//    private String requireField(CSVRecord record, String fieldName, int rowNumber) {
//        if (!record.isMapped(fieldName) || record.get(fieldName).isBlank()) {
//            throw new BusinessException(
//                    String.format("Row %d: required field '%s' is missing or empty.", rowNumber, fieldName)
//            );
//        }
//        return record.get(fieldName).trim();
//    }
//}