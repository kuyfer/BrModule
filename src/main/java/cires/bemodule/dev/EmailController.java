package cires.bemodule.dev;

import cires.bemodule.models.EmailPayload;
import cires.bemodule.services.EmailQueueProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailQueueProducer emailQueueProducer;

    public EmailController(EmailQueueProducer emailQueueProducer) {
        this.emailQueueProducer = emailQueueProducer;
    }

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailPayload payload) {
        emailQueueProducer.queueEmail(payload);
        return "Email job queued successfully!";
    }
}
