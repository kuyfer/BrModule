package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @GetMapping
    public void getAllParticipants() {}

    @PostMapping
    public void createParticipant(@RequestBody Object participant) {}

    @PostMapping("/import/session/{sessionId}")
    public void importParticipantsFromSession(@PathVariable Long sessionId, @RequestBody Object importRequest) {}
}