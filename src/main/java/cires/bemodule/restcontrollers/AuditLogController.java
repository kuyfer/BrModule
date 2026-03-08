package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    @GetMapping
    public void getAllAuditLogs() {}

    @GetMapping("/{id}")
    public void getAuditLogById(@PathVariable Long id) {}
}