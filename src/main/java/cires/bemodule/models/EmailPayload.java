package cires.bemodule.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Lightweight payload object used to carry email sending instructions
 * through the messaging / email queue.
 * <p>
 * Contains the recipient, subject, Thymeleaf template name, the template
 * variables, and an optional reference to the {@code Notification} entity
 * that triggered the email (for auditing purposes).
 * </p>
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmailPayload {

    /**
     * Recipient email address.
     */
    private String to;

    /**
     * Subject line of the email.
     */
    private String subject;

    /**
     * Name of the Thymeleaf template (without extension) to be used for
     * rendering the email body.
     */
    private String templateName;

    /**
     * Model variables passed to the Thymeleaf template.
     */
    private Map<String, Object> templateModel;

    /**
     * Optional database identifier of the {@code Notification} entity that
     * spawned this email, used for status tracking and audit logs.
     * May be {@code null} for system‑initiated emails.
     */
    private Long notificationId;

    /**
     * Convenience constructor for building a payload without a direct
     * notification reference.
     *
     * @param to           recipient email address
     * @param subject      email subject line
     * @param templateName Thymeleaf template name (without extension)
     * @param model        template variables
     */
    public EmailPayload(String to, String subject, String templateName, Map<String, Object> model) {
        this.to = to;
        this.subject = subject;
        this.templateName = templateName;
        this.templateModel = model;
    }
}