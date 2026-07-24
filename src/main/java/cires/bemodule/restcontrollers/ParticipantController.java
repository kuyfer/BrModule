package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CreateParticipantRequest;
import cires.bemodule.dtos.views.ParticipantDTO;
import cires.bemodule.dtos.responses.CreateParticipantResponse;
import cires.bemodule.dtos.requests.PatchParticipantRequest;
import cires.bemodule.enums.RegistrationSource;
import cires.bemodule.services.ParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    // ################################# CREATE ######################################

    @PostMapping
    @PreAuthorize("hasAuthority('participant:create')")
    public ResponseEntity<CreateParticipantResponse> createParticipant(
            @Valid @RequestBody CreateParticipantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(participantService.createParticipant(request));
    }

    // ################################# READ ########################################

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('participant:read')")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable Long id) {
        ParticipantDTO participant = participantService.findParticipantById(id);
        return ResponseEntity.ok(participant);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('participant:read')")
    public ResponseEntity<Page<ParticipantDTO>> getAllParticipants(
            @RequestParam(required = false) RegistrationSource source,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(participantService.findAll(source, pageable));
    }
    // ################################# UPDATE ######################################

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('participant:update')")
    public ResponseEntity<ParticipantDTO> patch(@PathVariable Long id,
                                                @RequestBody @Valid PatchParticipantRequest request) {
        return ResponseEntity.ok(participantService.patchParticipant(id, request));
    }

    // ################################# DELETE ######################################

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('participant:delete')")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        participantService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }
}