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

    public EmailPayload(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.templateName = "welcome";
        this.templateModel = Map.of("body", body);
    }

}
