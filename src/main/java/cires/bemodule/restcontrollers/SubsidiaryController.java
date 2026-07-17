package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CreateSubsidiaryRequest;
import cires.bemodule.dtos.requests.PatchSubsidiaryRequest;
import cires.bemodule.dtos.views.SubsidiaryDTO;
import cires.bemodule.services.SubsidiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subsidiaries")
public class SubsidiaryController {

    private final SubsidiaryService subsidiaryService;

    // ################################# CREATE ######################################

    @PostMapping
    @PreAuthorize("hasAuthority('subsidiary:create')")
    public ResponseEntity<SubsidiaryDTO> create(@Valid @RequestBody CreateSubsidiaryRequest request) {
        return ResponseEntity.ok(subsidiaryService.createSubsidiary(request));
    }

    // ################################# READ ########################################

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('subsidiary:read')")
    public ResponseEntity<SubsidiaryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subsidiaryService.findSubsidiaryById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('subsidiary:read')")
    public ResponseEntity<Page<SubsidiaryDTO>> getAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subsidiaryService.findAll(pageable, name));
    }

    // ################################# UPDATE ######################################

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('subsidiary:update')")
    public ResponseEntity<SubsidiaryDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PatchSubsidiaryRequest request) {
        return ResponseEntity.ok(subsidiaryService.patchSubsidiary(id, request));
    }

    // ################################# DELETE ######################################

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('subsidiary:delete')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subsidiaryService.deleteSubsidiary(id);
        return ResponseEntity.noContent().build();
    }
}