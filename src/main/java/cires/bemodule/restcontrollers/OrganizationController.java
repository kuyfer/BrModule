package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CreateOrganizationRequest;
import cires.bemodule.dtos.requests.PatchOrganizationRequest;
import cires.bemodule.dtos.views.OrganizationDTO;
import cires.bemodule.services.OrganizationService;
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
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    // ################################# CREATE ######################################

    @PostMapping
    @PreAuthorize("hasAuthority('organization:create')")
    public ResponseEntity<OrganizationDTO> create(@Valid @RequestBody CreateOrganizationRequest request) {
        return ResponseEntity.ok(organizationService.createOrganization(request));
    }

    // ################################# READ ########################################

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('organization:read')")
    public ResponseEntity<OrganizationDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.findOrganizationById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('organization:read')")
    public ResponseEntity<Page<OrganizationDTO>> getAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(organizationService.findAll(pageable, name));
    }

    // ################################# UPDATE ######################################

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('organization:update')")
    public ResponseEntity<OrganizationDTO> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchOrganizationRequest request) {
        return ResponseEntity.ok(organizationService.patchOrganization(id, request));
    }

    // ################################# DELETE ######################################

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('organization:delete')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}