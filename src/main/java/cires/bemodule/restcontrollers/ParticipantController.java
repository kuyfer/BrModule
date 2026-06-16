package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CreateParticipantRequest;
import cires.bemodule.dtos.views.ParticipantDTO;
import cires.bemodule.dtos.responses.CreateParticipantResponse;
import cires.bemodule.dtos.requests.PatchParticipantRequest;
import cires.bemodule.enums.RegistrationSource;
import cires.bemodule.mappers.ParticipantMapper;
import cires.bemodule.services.ParticipantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;
    private final ParticipantMapper participantMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) {
        ParticipantDTO participant = participantService.findParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants(
            @RequestParam(required = false) RegistrationSource source)
          {
        List<ParticipantDTO> participants = participantService.findAll(source);
        return ResponseEntity.ok(participants);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ParticipantDTO> patch(@PathVariable Long id,
                                                @RequestBody @Valid PatchParticipantRequest request) {
        return ResponseEntity.ok(participantService.patchParticipant(id, request));
    }
    @PostMapping
    public ResponseEntity<CreateParticipantResponse> createParticipant(
            @Valid @RequestBody CreateParticipantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(participantService.createParticipant(request));
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