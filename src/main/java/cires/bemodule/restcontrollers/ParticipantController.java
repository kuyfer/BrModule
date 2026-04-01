package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.CreateParticipantRequest;
import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.entities.Participant;
import cires.bemodule.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {this.participantService = participantService;}

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) {
        ParticipantDTO participant = participantService.getParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants() {
        List<ParticipantDTO> participants = participantService.allParticipants();
        return ResponseEntity.ok(participants);
    }

    @PostMapping
    public void createParticipant(@RequestBody CreateParticipantRequest request) {
        Participant participant = participantService.createParticipant(request);
    }

    @PostMapping("/import/session/{sessionId}")
    public void importParticipantsFromSession(@PathVariable Long sessionId, @RequestBody Object importRequest) {}

    @PreAuthorize( "hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

}