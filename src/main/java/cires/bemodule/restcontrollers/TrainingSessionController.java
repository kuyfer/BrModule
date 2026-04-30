package cires.bemodule.restcontrollers;

import cires.bemodule.dtos2.CreateTrainingSessionRequest;
import cires.bemodule.dtos2.CreateTrainingSessionResponse;
import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.services.TrainingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;

    public TrainingSessionController(TrainingSessionService trainingSessionService) {this.trainingSessionService = trainingSessionService;}

    @GetMapping
    public void getAllSessions() {}

    @PostMapping
    public ResponseEntity<CreateTrainingSessionResponse> createSession(@Valid @RequestBody CreateTrainingSessionRequest session) {
        trainingSessionService.createTrainingSession(session);
        CreateTrainingSessionResponse response = new CreateTrainingSessionResponse("created");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSessionDTO> getSessionById(@PathVariable Long id) {
        TrainingSessionDTO session = trainingSessionService.findTrainingSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PatchMapping("/{id}/status")
    public void updateSessionStatus(@PathVariable Long id, @RequestBody Object statusUpdate) {}
}