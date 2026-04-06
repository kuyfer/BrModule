package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.services.TrainingSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class TrainingSessionController {

    private final TrainingSessionService TrainingSessionService;

    public TrainingSessionController(TrainingSessionService TrainingSessionService) {this.TrainingSessionService = TrainingSessionService;}

    @GetMapping
    public void getAllSessions() {}

    @PostMapping
    public ResponseEntity<TrainingSession> createSession(@RequestBody TrainingSession session) {
        TrainingSession trainingSession = TrainingSessionService.createTrainingSession(session);
        return ResponseEntity.ok(trainingSession);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingSessionDTO> getSessionById(@PathVariable Long id) {
        TrainingSessionDTO session = TrainingSessionService.getTrainingSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PatchMapping("/{id}/status")
    public void updateSessionStatus(@PathVariable Long id, @RequestBody Object statusUpdate) {}
}