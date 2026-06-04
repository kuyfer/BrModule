package cires.bemodule.restcontrollers;


import cires.bemodule.dtos.requests.CancelTrainingSessionRequest;
import cires.bemodule.dtos.requests.CreateTrainingSessionRequest;
import cires.bemodule.dtos.responses.CreateTrainingSessionResponse;
import cires.bemodule.dtos.views.TrainingSessionDTO;
import cires.bemodule.dtos.requests.UpdateTrainingSessionsRequest;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.services.TrainingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {this.trainingSessionService = trainingSessionService;}

    @PostMapping
    public ResponseEntity<CreateTrainingSessionResponse> createSession(@Valid @RequestBody CreateTrainingSessionRequest session) {
        trainingSessionService.createTrainingSession(session);
        CreateTrainingSessionResponse response = new CreateTrainingSessionResponse("created");
        return ResponseEntity.ok(response);

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

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSessionDTO> getSessionById(@PathVariable Long id) {
        TrainingSessionDTO session = trainingSessionService.findTrainingSessionById(id);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id){

        trainingSessionService.deleteTrainingSession(id);
        return ResponseEntity.noContent().build();

}


    @GetMapping
    public ResponseEntity<List<TrainingSessionDTO>> getAllTrainingSessions(
            @RequestParam(required = false) TrainingSessionStatus status,
            @RequestParam(required = false) TrainingSessionMode mode
    ) {
        List<TrainingSessionDTO> sessions = trainingSessionService.findAll(status, mode);
        return ResponseEntity.ok(sessions);
    }

}