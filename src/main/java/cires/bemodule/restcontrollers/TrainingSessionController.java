package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @GetMapping
    public void getAllSessions() {}

    @PostMapping
    public void createSession(@RequestBody Object session) {}

    @GetMapping("/{id}")
    public void getSessionById(@PathVariable Long id) {}

    @PatchMapping("/{id}/status")
    public void updateSessionStatus(@PathVariable Long id, @RequestBody Object statusUpdate) {}
}