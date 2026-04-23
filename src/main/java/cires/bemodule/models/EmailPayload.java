package cires.bemodule.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmailPayload {
    private String to;
    private String subject;
    private String templateName;
    private Map<String, Object> templateModel;
    private Long notificationId;

    public EmailPayload(String email, String subject, String s, Map<String, Object> model) {
            this.to = email;
            this.subject = subject;
            this.templateName = s;
            this.templateModel = model;
    }
}
