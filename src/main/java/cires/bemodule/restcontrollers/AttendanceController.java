package cires.bemodule.restcontrollers;

import cires.bemodule.security.models.UserPrincipal;
import cires.bemodule.services.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import cires.bemodule.dtos.requests.*;
import cires.bemodule.dtos.responses.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    // ─── MARK SINGLE ──────────────────────────────────────────────────────────

    @PostMapping
    @PreAuthorize("hasAnyRole('FORMATEUR', 'GESTIONNAIRE_FORMATION', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<AttendanceResponse> markAttendance(
            @Valid @RequestBody MarkAttendanceRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request, principal.getId()));
    }

    // ─── BULK MARK ────────────────────────────────────────────────────────────

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('FORMATEUR', 'GESTIONNAIRE_FORMATION', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<BulkMarkResult> bulkMarkAttendance(
            @Valid @RequestBody BulkMarkAttendanceRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(attendanceService.bulkMarkAttendance(request, principal.getId()));
    }

    // ─── TRAINER VALIDATION ───────────────────────────────────────────────────

    @PostMapping("/validate-day")
    @PreAuthorize("hasAnyRole('FORMATEUR', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<Void> validateDay(
            @Valid @RequestBody ValidateDayRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        attendanceService.validateDayAttendance(
                request.getSessionId(), request.getDate(), principal.getId());
        return ResponseEntity.noContent().build();
    }

    // ─── ADMIN CORRECTION ─────────────────────────────────────────────────────

    @PatchMapping("/{id}/correct")
    @PreAuthorize("hasAnyRole('ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<AttendanceResponse> correct(
            @PathVariable Long id,
            @Valid @RequestBody CorrectAttendanceRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(
                attendanceService.correctAttendance(id, request, principal.getId()));
    }

    // ─── READ SINGLE ──────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('FORMATEUR', 'GESTIONNAIRE_FORMATION', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<AttendanceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.getById(id));
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

//    @GetMapping
//    @PreAuthorize("hasAnyRole('GESTIONNAIRE_FORMATION', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
//    public ResponseEntity<Page<AttendanceResponse>> search(
//            AttendanceFilterRequest filter,
//            @PageableDefault(size = 20, sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
//
//        return ResponseEntity.ok(attendanceService.search(filter, pageable));
//    }

    // ─── GRID ─────────────────────────────────────────────────────────────────

    @GetMapping("/session/{sessionId}/grid")
    @PreAuthorize("hasAnyRole('FORMATEUR', 'GESTIONNAIRE_FORMATION', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<List<AttendanceDayGrid>> getGrid(@PathVariable Long sessionId) {
        return ResponseEntity.ok(attendanceService.getGridForSession(sessionId));
    }

    // ─── PARTICIPANT SUMMARY ──────────────────────────────────────────────────

    @GetMapping("/session/{sessionId}/participant/{participantId}/summary")
    @PreAuthorize("hasAnyRole('FORMATEUR', 'GESTIONNAIRE_FORMATION', 'ADMIN_FONCTIONNEL', 'SUPER_ADMIN')")
    public ResponseEntity<ParticipantAttendanceSummary> getParticipantSummary(
            @PathVariable Long sessionId,
            @PathVariable Long participantId) {

        return ResponseEntity.ok(
                attendanceService.getSummaryForParticipant(sessionId, participantId));
    }
}