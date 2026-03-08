package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    @GetMapping("/sessions/{id}/attendance")
    public void getAttendanceBySession(@PathVariable Long id) {}

    @PostMapping("/attendance/bulk")
    public void bulkCreateAttendance(@RequestBody Object bulkRequest) {}

    @PostMapping("/attendance/validate")
    public void validateAttendance(@RequestBody Object validationRequest) {}
}