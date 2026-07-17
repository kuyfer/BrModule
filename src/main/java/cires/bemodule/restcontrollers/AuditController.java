package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.AuditFilterRequest;
import cires.bemodule.dtos.RevisionDetail;
import cires.bemodule.services.AuditService;
import cires.bemodule.mappers.EntityTypeMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.RevisionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/{entityType}/{id}")
   // @PreAuthorize("hasAuthority('audit:read') or hasRole('ADMIN')")
    public ResponseEntity<Page<RevisionDetail>> getRevisions(
            @PathVariable String entityType,
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String ipAddress) {

        Class<?> clazz = EntityTypeMapper.fromString(entityType);
        Pageable pageable = PageRequest.of(page, size);
        Page<RevisionDetail> revisions = auditService.getRevisionsForEntity(clazz, id, pageable, username, ipAddress);
        return ResponseEntity.ok(revisions);
    }

    @GetMapping("/{entityType}/{id}/revision/{rev}")
   // @PreAuthorize("hasAuthority('audit:read') or hasRole('ADMIN')")
    public ResponseEntity<Object> getSnapshot(
            @PathVariable String entityType,
            @PathVariable Long id,
            @PathVariable int rev) {

        Class<?> clazz = EntityTypeMapper.fromString(entityType);
        Object snapshot = auditService.getEntityAtRevision(clazz, id, rev);
        return ResponseEntity.ok(snapshot);
    }

    @GetMapping("/recent")
   // @PreAuthorize("hasAuthority('audit:read') or hasRole('ADMIN')")
    public ResponseEntity<List<RevisionDetail>> getRecentChanges(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(auditService.getRecentGlobalChanges(limit));
    }

    @GetMapping("/global")
   // @PreAuthorize("hasAuthority('audit:read') or hasRole('ADMIN')")
    public ResponseEntity<Page<RevisionDetail>> getGlobalRevisions(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(required = false) RevisionType action,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<RevisionDetail> revisions = auditService.getFilteredGlobalRevisions(
                username, ipAddress, action, fromDate, toDate, pageable);
        return ResponseEntity.ok(revisions);
    }

    @PostMapping("/{entityType}/{id}/filter")
   // @PreAuthorize("hasAuthority('audit:read') or hasRole('ADMIN')")
    public ResponseEntity<List<RevisionDetail>> filterRevisions(
            @PathVariable String entityType,
            @PathVariable Long id,
            @RequestBody AuditFilterRequest filter,
            @RequestParam(defaultValue = "100") int maxResults) {

        Class<?> clazz = EntityTypeMapper.fromString(entityType);
        List<RevisionDetail> results = auditService.getRevisionsWithFilters(clazz, id, filter, maxResults);
        return ResponseEntity.ok(results);
    }
}