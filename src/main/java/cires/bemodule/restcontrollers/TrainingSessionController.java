package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CancelTrainingSessionRequest;
import cires.bemodule.dtos.requests.CreateTrainingSessionRequest;
import cires.bemodule.dtos.requests.PostponeTrainingSessionRequest;
import cires.bemodule.dtos.views.TrainingSessionDTO;
import cires.bemodule.dtos.requests.UpdateTrainingSessionsRequest;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.services.TrainingSessionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('session:read')")
    public ResponseEntity<TrainingSessionDTO> getSessionById(@PathVariable Long id) {
        TrainingSessionDTO session = trainingSessionService.findTrainingSessionById(id);
        return ResponseEntity.ok(session);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('session:read')")
    public ResponseEntity<Page<TrainingSessionDTO>> getAllTrainingSessions(
            @RequestParam(required = false) TrainingSessionStatus status,
            @RequestParam(required = false) TrainingSessionMode mode,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<TrainingSessionDTO> sessions = trainingSessionService.findAll(status, mode, pageable);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('session:create')")
    public ResponseEntity<TrainingSessionDTO> createSession(@Valid @RequestBody CreateTrainingSessionRequest request) {
        TrainingSessionDTO session = trainingSessionService.createTrainingSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('session:update')")
    public ResponseEntity<Void> cancelSession(@PathVariable Long id,@Valid @RequestBody CancelTrainingSessionRequest request) {
        trainingSessionService.cancelSession(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/postpone")
    @PreAuthorize("hasAuthority('session:update')")
    public ResponseEntity<TrainingSessionDTO> postponeSession(
            @PathVariable Long id,
            @Valid @RequestBody PostponeTrainingSessionRequest request) {
        return ResponseEntity.ok(trainingSessionService.postponeSession(id, request));
    }

    @PostMapping("/{id}/status")
    @PreAuthorize("hasAuthority('session:update')")
    public ResponseEntity<Void> updateSession(@PathVariable Long id, @RequestBody UpdateTrainingSessionsRequest request ) {
        trainingSessionService.changeStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasAuthority('session:update')")
    public ResponseEntity<TrainingSessionDTO> publishSession(@PathVariable Long id) {
        return ResponseEntity.ok(trainingSessionService.publishSession(id));
    }

    @PostMapping("/{id}/participants")
    @PreAuthorize("hasAuthority('session:update')")
    public ResponseEntity<Void> addParticipants(
            @PathVariable Long id,
            @RequestBody List<Long> participantIds) {

        trainingSessionService.addParticipants(id, participantIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('session:delete')")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id){

        trainingSessionService.deleteTrainingSession(id);
        return ResponseEntity.noContent().build();
    }
}