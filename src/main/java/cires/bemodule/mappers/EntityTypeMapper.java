package cires.bemodule.mappers;

import cires.bemodule.entities.*;

import java.util.Map;

public class EntityTypeMapper {

    private EntityTypeMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static final Map<String, Class<?>> MAP = Map.ofEntries(
            Map.entry("user", User.class),
            Map.entry("session", TrainingSession.class),
            Map.entry("participant", Participant.class),
            Map.entry("attendance", Attendance.class),
            Map.entry("trainer", Trainer.class),
            Map.entry("organization", Organization.class),
            Map.entry("subsidiary", Subsidiary.class),
            Map.entry("notification", Notification.class),
            Map.entry("role", Role.class),
            Map.entry("permission", Permission.class),
            Map.entry("sessionparticipant", SessionParticipant.class),
            Map.entry("resettoken", ResetToken.class),
            Map.entry("exporthistory", ExportHistory.class),
            Map.entry("dashboard", Dashboard.class)
    );

    public static Class<?> fromString(String type) {
        Class<?> clazz = MAP.get(type.toLowerCase());
        if (clazz == null) {
            throw new IllegalArgumentException("Unknown entity type: " + type);
        }
        return clazz;
    }
}