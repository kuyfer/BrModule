package cires.bemodule.dtos.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class SessionPresenceStat {
    private Long   sessionId;
    private String sessionTitle;
    private String trainerName;
    private double presenceRate;
    private long   totalParticipants;
}