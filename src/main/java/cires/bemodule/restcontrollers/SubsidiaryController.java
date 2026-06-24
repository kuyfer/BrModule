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
@RequestMapping("/api/subsidiaries")
@RequiredArgsConstructor
public class SubsidiaryController {

    private final SubsidiaryService subsidiaryService;

    @PostMapping
    public ResponseEntity<SubsidiaryDTO> create(@Valid @RequestBody CreateSubsidiaryRequest request) {
        return ResponseEntity.ok(subsidiaryService.createSubsidiary(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubsidiaryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subsidiaryService.findSubsidiaryById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SubsidiaryDTO>> getAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(subsidiaryService.findAll(pageable, name));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<SubsidiaryDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PatchSubsidiaryRequest request) {
        return ResponseEntity.ok(subsidiaryService.patchSubsidiary(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subsidiaryService.deleteSubsidiary(id);
        return ResponseEntity.noContent().build();
    }
}