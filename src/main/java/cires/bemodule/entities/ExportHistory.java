package cires.bemodule.entities;

import cires.bemodule.enums.ExportFormat;
import cires.bemodule.enums.ExportStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "export_history")
public class ExportHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportStatus exportStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportFormat exportFormat;

    @Column(nullable = false)
    private String exportedBy;

    @Column(nullable = false)
    private LocalDateTime exportedAt;

    @Column(nullable = false)
    private String entityType;

    @Column(length = 512)
    private String fileName;
}