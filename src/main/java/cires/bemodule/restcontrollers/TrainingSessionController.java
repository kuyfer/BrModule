package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CancelTrainingSessionRequest;
import cires.bemodule.dtos.requests.CreateTrainingSessionRequest;
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
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSessionDTO> getSessionById(@PathVariable Long id) {
        TrainingSessionDTO session = trainingSessionService.findTrainingSessionById(id);
        return ResponseEntity.ok(session);
    }

    @GetMapping
    public ResponseEntity<Page<TrainingSessionDTO>> getAllTrainingSessions(
            @RequestParam(required = false) TrainingSessionStatus status,
            @RequestParam(required = false) TrainingSessionMode mode,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<TrainingSessionDTO> sessions = trainingSessionService.findAll(status, mode, pageable);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping
    public ResponseEntity<TrainingSessionDTO> createSession(@Valid @RequestBody CreateTrainingSessionRequest request) {
        TrainingSessionDTO session = trainingSessionService.createTrainingSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> cancelSession(@PathVariable Long id,@Valid @RequestBody CancelTrainingSessionRequest request) {
        trainingSessionService.cancelSession(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateSession(@PathVariable Long id, @RequestBody UpdateTrainingSessionsRequest request ) {
        trainingSessionService.changeStatus(id, request.getStatus());
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id){

        trainingSessionService.deleteTrainingSession(id);
        return ResponseEntity.noContent().build();

}

}