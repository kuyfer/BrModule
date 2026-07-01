package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.responses.ExecutiveDashboardResponse;
import cires.bemodule.dtos.responses.OperationalDashboardResponse;
import cires.bemodule.dtos.responses.TrainerDashboardResponse;
import cires.bemodule.security.models.UserPrincipal;
import cires.bemodule.services.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/executive")
    @PreAuthorize("hasAuthority('dashboard:executive')")
    public ResponseEntity<ExecutiveDashboardResponse> executive() {
        return ResponseEntity.ok(dashboardService.getExecutiveDashboard());
    }

    @GetMapping("/operational")
    @PreAuthorize("hasAuthority('dashboard:operational')")
    public ResponseEntity<OperationalDashboardResponse> operational() {
        return ResponseEntity.ok(dashboardService.getOperationalDashboard());
    }

    @GetMapping("/trainer")
    @PreAuthorize("hasAuthority('dashboard:trainer')")
    public ResponseEntity<TrainerDashboardResponse> trainer(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(
                dashboardService.getTrainerDashboard(principal.getId()));
    }

//    @GetMapping("/audit")
//    @PreAuthorize("hasAuthority('dashboard:audit')")
//    public ResponseEntity<AuditDashboardResponse> audit() {
//        return ResponseEntity.ok(dashboardService.getAuditDashboard());
//    }
}