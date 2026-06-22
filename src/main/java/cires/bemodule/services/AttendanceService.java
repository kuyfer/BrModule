package cires.bemodule.services;

import cires.bemodule.entities.Attendance;
import cires.bemodule.entities.Participant;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.AttendanceSlot;
import cires.bemodule.enums.AttendanceStatus;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.exceptions.controllerexceptions.ParticipantNotFoundException;
import cires.bemodule.exceptions.controllerexceptions.TrainingSessionNotFoundException;
import cires.bemodule.mappers.AttendanceMapper;
import cires.bemodule.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cires.bemodule.dtos.responses.AttendanceResponse;
import cires.bemodule.dtos.responses.ParticipantAttendanceSummary;
import cires.bemodule.dtos.responses.AttendanceDayGrid;
import cires.bemodule.dtos.responses.SlotGrid;
import cires.bemodule.dtos.responses.SlotEntry;
import cires.bemodule.dtos.requests.BulkAttendanceEntry;
import cires.bemodule.dtos.requests.BulkMarkAttendanceRequest;
import cires.bemodule.dtos.requests.MarkAttendanceRequest;
import cires.bemodule.dtos.requests.CorrectAttendanceRequest;
import cires.bemodule.dtos.responses.BulkMarkResult;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository        attendanceRepository;
    private final TrainingSessionRepository sessionRepository;
    private final ParticipantRepository participantRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final TrainerRepository trainerRepository;
    private final AttendanceMapper attendanceMapper;

    // ─── MARK ─────────────────────────────────────────────────────────────────

    public AttendanceResponse markAttendance(MarkAttendanceRequest request) {
        TrainingSession session = findSessionOrThrow(request.getSessionId());
        assertSessionIsInProgress(session);
        assertDateWithinSession(request.getDate(), session);
        assertParticipantEnrolled(request.getSessionId(), request.getParticipantId());
        assertDayNotValidated(request.getSessionId(), request.getDate());

        if (request.getStatus() == AttendanceStatus.LATE && isBlank(request.getDelayReason())) {
            throw new RuntimeException("Delay reason is required when status is LATE.");
        }

        Attendance attendance = attendanceRepository
                .findBySessionIdAndParticipantIdAndDateAndSlot(
                        request.getSessionId(),
                        request.getParticipantId(),
                        request.getDate(),
                        request.getSlot())
                .orElseGet(Attendance::new);

        boolean isNew = attendance.getId() == null;

        attendance.setSession(session);
        attendance.setParticipant(resolveParticipant(request.getParticipantId()));
        attendance.setDate(request.getDate());
        attendance.setSlot(request.getSlot());
        attendance.setStatus(request.getStatus());
        attendance.setDelayReason(request.getDelayReason());
        attendance.setComment(request.getComment());
        attendance.setValidated(false);

        Attendance saved = attendanceRepository.save(attendance);
        log.info("Attendance {} [session={}, participant={}, date={}, slot={}, status={}]",
                isNew ? "created" : "updated",
                request.getSessionId(), request.getParticipantId(),
                request.getDate(), request.getSlot(), request.getStatus());

        return attendanceMapper.toResponse(saved);
    }

    // ─── BULK MARK ────────────────────────────────────────────────────────────

    public BulkMarkResult bulkMarkAttendance(BulkMarkAttendanceRequest request) {
        if (request.getEntries() == null || request.getEntries().isEmpty()) {
            throw new RuntimeException("Bulk request must contain at least one entry.");
        }

        List<AttendanceResponse> results = new ArrayList<>();
        List<String>             errors  = new ArrayList<>();

        for (BulkAttendanceEntry entry : request.getEntries()) {
            try {
                MarkAttendanceRequest single = MarkAttendanceRequest.builder()
                        .sessionId(request.getSessionId())
                        .date(request.getDate())
                        .slot(request.getSlot())
                        .participantId(entry.getParticipantId())
                        .status(entry.getStatus())
                        .delayReason(entry.getDelayReason())
                        .comment(entry.getComment())
                        .build();
                results.add(markAttendance(single));
            } catch (RuntimeException e) {
                errors.add("Participant " + entry.getParticipantId() + ": " + e.getMessage());
            }
        }

        log.info("Bulk mark complete [session={}, date={}, slot={}, success={}, errors={}]",
                request.getSessionId(), request.getDate(), request.getSlot(),
                results.size(), errors.size());

        return BulkMarkResult.builder()
                .totalEntries(request.getEntries().size())
                .successCount(results.size())
                .errorCount(errors.size())
                .errors(errors)
                .results(results)
                .build();
    }

    // ─── TRAINER VALIDATION ───────────────────────────────────────────────────

    public void validateDayAttendance(Long sessionId, LocalDate date, Long trainerId) {
        TrainingSession session = findSessionOrThrow(sessionId);
        assertSessionIsInProgress(session);
        assertDateWithinSession(date, session);

        // Only the assigned trainer can validate
        if (!session.getTrainer().getUser().getId().equals(trainerId)) {
            throw new RuntimeException("Only the assigned trainer may validate attendance.");
        }

        // All enrolled participants must have both slots marked
        List<Long> enrolledIds = sessionParticipantRepository
                .findParticipantIdsByTrainingSessionId(sessionId);
        List<Long> markedIds   = attendanceRepository
                .findParticipantIdsMarkedForDay(sessionId, date);

        List<Long> unmarked = enrolledIds.stream()
                .filter(id -> !markedIds.contains(id))
                .toList();

        if (!unmarked.isEmpty()) {
            throw new RuntimeException(
                    unmarked.size() + " participant(s) have no attendance record for " + date +
                            ". Mark all participants before validating."
            );
        }

        int updated = attendanceRepository.markDayAsValidated(
                sessionId, date, trainerId, LocalDateTime.now());

        log.info("Day validated [session={}, date={}, trainer={}, records={}]",
                sessionId, date, trainerId, updated);
    }

    // ─── ADMIN CORRECTION ─────────────────────────────────────────────────────

    public AttendanceResponse correctAttendance(Long attendanceId,
                                                CorrectAttendanceRequest request,
                                                Long correctedByUserId) {
        Attendance attendance = findAttendanceOrThrow(attendanceId);

        if (request.getStatus() == AttendanceStatus.LATE && isBlank(request.getDelayReason())) {
            throw new RuntimeException("Delay reason is required when correcting to LATE.");
        }

        // Append to audit note — never overwrite it
        String newNote = String.format("[CORRECTION by userId=%s at %s] %s → %s. Reason: %s",
                correctedByUserId, LocalDateTime.now(),
                attendance.getStatus(), request.getStatus(),
                request.getCorrectionReason());

        String existingNote = attendance.getAuditNote();
        attendance.setAuditNote(existingNote == null ? newNote : existingNote + "\n" + newNote);

        attendance.setStatus(request.getStatus());
        attendance.setDelayReason(request.getDelayReason());
        attendance.setComment(request.getComment());
        attendance.setCorrectionReason(request.getCorrectionReason());

        Attendance saved = attendanceRepository.save(attendance);
        log.warn("Attendance corrected [id={}, newStatus={}, by={}]",
                attendanceId, request.getStatus(), correctedByUserId);

        return attendanceMapper.toResponse(saved);
    }

    // ─── READ ──────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public AttendanceResponse getById(Long id) {
        return attendanceMapper.toResponse(findAttendanceOrThrow(id));
    }

//    @Transactional(readOnly = true)
//    public Page<AttendanceResponse> search(AttendanceFilterRequest filter, Pageable pageable) {
//        return attendanceRepository.search(filter, pageable)
//                .map(attendanceMapper::toResponse);
//    }

    /**
     * Builds the full attendance grid for a session.
     * Each row is one day. Each row has an AM column and a PM column.
     * Each column lists every enrolled participant with their status (or null if unmarked).
     */
    @Transactional(readOnly = true)
    public List<AttendanceDayGrid> getGridForSession(Long sessionId) {
        findSessionOrThrow(sessionId);

        List<Attendance> records     = attendanceRepository
                .findAllBySessionIdOrderByDateAscSlotAsc(sessionId);
        List<Long> enrolledIds       = sessionParticipantRepository
                .findParticipantIdsByTrainingSessionId(sessionId);

        Map<LocalDate, List<Attendance>> byDate = records.stream()
                .collect(Collectors.groupingBy(Attendance::getDate));

        return byDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    Map<AttendanceSlot, List<Attendance>> bySlot = entry.getValue().stream()
                            .collect(Collectors.groupingBy(Attendance::getSlot));

                    List<Attendance> amRecords = bySlot.getOrDefault(AttendanceSlot.AM, List.of());
                    List<Attendance> pmRecords = bySlot.getOrDefault(AttendanceSlot.PM, List.of());

                    boolean dayValidated = entry.getValue().stream()
                            .anyMatch(Attendance::isValidated);

                    return AttendanceDayGrid.builder()
                            .date(date)
                            .dayValidated(dayValidated)
                            .totalEnrolled(enrolledIds.size())
                            .amSlot(buildSlotGrid(amRecords, enrolledIds))
                            .pmSlot(buildSlotGrid(pmRecords, enrolledIds))
                            .build();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public ParticipantAttendanceSummary getSummaryForParticipant(Long sessionId, Long participantId) {
        TrainingSession session = findSessionOrThrow(sessionId);
        assertParticipantEnrolled(sessionId, participantId);

        List<Attendance> records = attendanceRepository
                .findAllBySessionIdAndParticipantIdOrderByDateAscSlotAsc(sessionId, participantId);

        long total     = records.size();
        long present   = count(records, AttendanceStatus.PRESENT);
        long late      = count(records, AttendanceStatus.LATE);
        long absent    = count(records, AttendanceStatus.ABSENT);
        long justified = count(records, AttendanceStatus.JUSTIFIED_ABSENCE);
        long unmarked  = computeExpectedSlots(session) - total;
        double rate    = total > 0 ? (double)(present + late) / total * 100 : 0.0;

        Participant p = resolveParticipant(participantId);

        return ParticipantAttendanceSummary.builder()
                .participantId(participantId)
                .participantFullName(p.getFirstName() + " " + p.getLastName())
                .sessionId(sessionId)
                .sessionTitle(session.getTitle())
                .totalSlots(total)
                .presentCount(present)
                .lateCount(late)
                .absentCount(absent)
                .justifiedCount(justified)
                .unmarkedCount(Math.max(0, unmarked))
                .presenceRate(Math.round(rate * 10.0) / 10.0)
                .build();
    }

    // ─── PRIVATE HELPERS ──────────────────────────────────────────────────────

    private SlotGrid buildSlotGrid(List<Attendance> slotRecords, List<Long> enrolledIds) {
        Map<Long, Attendance> byParticipant = slotRecords.stream()
                .collect(Collectors.toMap(
                        a -> a.getParticipant().getId(), a -> a));

        List<SlotEntry> entries = enrolledIds.stream()
                .map(id -> {
                    Attendance a = byParticipant.get(id);
                    String fullName = a != null
                            ? a.getParticipant().getFirstName() + " " + a.getParticipant().getLastName()
                            : null;
                    return SlotEntry.builder()
                            .participantId(id)
                            .participantFullName(fullName)
                            .status(a != null ? a.getStatus() : null)
                            .delayReason(a != null ? a.getDelayReason() : null)
                            .comment(a != null ? a.getComment() : null)
                            .validated(a != null && a.isValidated())
                            .build();
                })
                .toList();

        return SlotGrid.builder()
                .presentCount(count(slotRecords,   AttendanceStatus.PRESENT))
                .lateCount(count(slotRecords,       AttendanceStatus.LATE))
                .absentCount(count(slotRecords,     AttendanceStatus.ABSENT))
                .justifiedCount(count(slotRecords,  AttendanceStatus.JUSTIFIED_ABSENCE))
                .unmarkedCount(enrolledIds.size() - slotRecords.size())
                .entries(entries)
                .build();
    }

    private TrainingSession findSessionOrThrow(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new TrainingSessionNotFoundException(sessionId));
    }

    private Attendance findAttendanceOrThrow(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found: " + id));
    }

    private Participant resolveParticipant(Long participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException(participantId));
    }

    private void assertSessionIsInProgress(TrainingSession session) {
        if (session.getStatus() != TrainingSessionStatus.ONGOING) {
            throw new RuntimeException(
                    "Attendance can only be recorded for ONGOING sessions. Current status: "
                            + session.getStatus());
        }
    }

    private void assertDateWithinSession(LocalDate date, TrainingSession session) {
        LocalDate start = session.getStartDate().toLocalDate();
        LocalDate end   = session.getEndDate().toLocalDate();
        if (date.isBefore(start) || date.isAfter(end)) {
            throw new RuntimeException(
                    "Date " + date + " is outside session range [" + start + " – " + end + "].");
        }
    }

    private void assertParticipantEnrolled(Long sessionId, Long participantId) {
        if (!sessionParticipantRepository.existsByTrainingSessionIdAndParticipantId(sessionId, participantId)) {
            throw new RuntimeException("Participant " + participantId + " is not enrolled in session " + sessionId);
        }
    }

    private void assertDayNotValidated(Long sessionId, LocalDate date) {
        if (attendanceRepository.isDayValidatedForSession(sessionId, date)) {
            throw new RuntimeException(
                    "Attendance for " + date + " has been validated. Use admin correction to override.");
        }
    }

    private long count(List<Attendance> records, AttendanceStatus status) {
        return records.stream().filter(a -> a.getStatus() == status).count();
    }

    /**
     * Total expected slots = number of session days × 2 (AM + PM)
     */
    private long computeExpectedSlots(TrainingSession session) {
        long days = ChronoUnit.DAYS.between(
                session.getStartDate().toLocalDate(),
                session.getEndDate().toLocalDate()) + 1;
        return days * 2;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}