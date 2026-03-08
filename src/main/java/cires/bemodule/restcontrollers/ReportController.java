package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @GetMapping("/attendance")
    public void getAttendanceReport() {}

    @GetMapping("/sessions")
    public void getSessionsReport() {}

    @GetMapping("/participants")
    public void getParticipantsReport() {}

    @GetMapping("/kpis")
    public void getKpisReport() {}
}