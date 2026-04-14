package cires.bemodule.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class EmailPayload {
    private String to;
    private String subject;
    private String body;
}
