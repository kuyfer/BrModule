package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/executive")
    public void getExecutiveDashboard() {}

    @GetMapping("/operations")
    public void getOperationsDashboard() {}

    @GetMapping("/trainer")
    public void getTrainerDashboard() {}

    @GetMapping("/audit")
    public void getAuditDashboard() {}
}