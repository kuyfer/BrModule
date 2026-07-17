package cires.bemodule.restcontrollers;

import cires.bemodule.security.models.UserPrincipal;
import cires.bemodule.services.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import cires.bemodule.dtos.requests.*;
import cires.bemodule.dtos.responses.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ─── MARK SINGLE ──────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAuthority('attendance:mark')")
    public ResponseEntity<AttendanceResponse> markAttendance(
            @Valid @RequestBody MarkAttendanceRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request));
    }

    // ─── BULK MARK ────────────────────────────────────────────────────────────

    @PostMapping("/bulk")
    @PreAuthorize("hasAuthority('attendance:mark')")
    public ResponseEntity<BulkMarkResult> bulkMarkAttendance(
            @Valid @RequestBody BulkMarkAttendanceRequest request) {

        return ResponseEntity.ok(attendanceService.bulkMarkAttendance(request));
    }

    // ─── TRAINER VALIDATION ───────────────────────────────────────────────────

    @PostMapping("/validate-day")
    @PreAuthorize("hasAuthority('attendance:validate')")
    public ResponseEntity<Void> validateDay(
            @Valid @RequestBody ValidateDayRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        attendanceService.validateDayAttendance(
                request.getSessionId(), request.getDate(), principal.getId());
        return ResponseEntity.noContent().build();
    }

    // ─── ADMIN CORRECTION ─────────────────────────────────────────────────────

    @PatchMapping("/{id}/correct")
    @PreAuthorize("hasAuthority('attendance:correct')")
    public ResponseEntity<AttendanceResponse> correct(
            @PathVariable Long id,
            @Valid @RequestBody CorrectAttendanceRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                attendanceService.correctAttendance(id, request, principal.getId()));
    }

    // ─── READ SINGLE ──────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('attendance:read')")
    public ResponseEntity<AttendanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getById(id));
    }

    @GetMapping("/session/{sessionId}/grid")
    @PreAuthorize("hasAuthority('attendance:read')")
    public ResponseEntity<List<AttendanceDayGrid>> getGrid(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getGridForSession(sessionId));
    }

    @GetMapping("/session/{sessionId}/participant/{participantId}/summary")
    @PreAuthorize("hasAuthority('attendance:read')")
    public ResponseEntity<ParticipantAttendanceSummary> getParticipantSummary(
            @PathVariable Long sessionId,
            @PathVariable Long participantId) {

        return ResponseEntity.ok(
                attendanceService.getSummaryForParticipant(sessionId, participantId));
    }
}