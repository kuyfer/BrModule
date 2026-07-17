package cires.bemodule.dtos;

import lombok.Data;
import org.hibernate.envers.RevisionType;
import java.time.LocalDateTime;

@Data
public class AuditFilterRequest {
    private String username;
    private String ipAddress;
    private RevisionType action;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}