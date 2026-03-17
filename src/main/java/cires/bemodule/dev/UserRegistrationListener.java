package cires.bemodule.dev;

import cires.bemodule.services.MailService;
import jakarta.mail.MessagingException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationListener {

    private final MailService mailService;

    public UserRegistrationListener(MailService mailService) {
        this.mailService = mailService;
    }

    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        String html = "<h1>Welcome!</h1><p>Thanks for signing up. We’re glad you’re here.</p>";
        try {
            mailService.sendHtml(event.email(), "Welcome to Our Service", html);
        } catch (MessagingException e) {

        }
    }
}