package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @GetMapping
    public void getAllNotifications() {}

    @PostMapping("/send")
    public void sendNotification(@RequestBody Object notification) {}

    @PostMapping("/{id}/retry")
    public void retryNotification(@PathVariable Long id) {}
}