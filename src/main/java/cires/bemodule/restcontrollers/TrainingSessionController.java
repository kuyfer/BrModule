package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.CreateTrainingSessionRequest;
import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.services.TrainingSessionService;
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
    public ResponseEntity<TrainingSessionDTO> createSession(@RequestBody CreateTrainingSessionRequest session) {
        TrainingSessionDTO trainingSession = trainingSessionService.createTrainingSession(session);
        return ResponseEntity.ok(trainingSession);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSessionDTO> getSessionById(@PathVariable Long id) {
        TrainingSessionDTO session = trainingSessionService.getTrainingSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PatchMapping("/{id}/status")
    public void updateSessionStatus(@PathVariable Long id, @RequestBody Object statusUpdate) {}
}