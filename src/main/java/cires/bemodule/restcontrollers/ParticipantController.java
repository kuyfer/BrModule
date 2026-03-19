package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.CreateParticipantRequest;
import cires.bemodule.entities.Participant;
import cires.bemodule.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        Participant participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
}

    @GetMapping
    public ResponseEntity<List<Participant>> getAllParticipants() {
        List<Participant> participants = participantService.allParticipants();
        return ResponseEntity.ok(participants);

    }

    @PostMapping
    public void createParticipant(@RequestBody CreateParticipantRequest request) {
        Participant participant = participantService.CreateParticipant(request);
    }

    @PostMapping("/import/session/{sessionId}")
    public void importParticipantsFromSession(@PathVariable Long sessionId, @RequestBody Object importRequest) {}
}