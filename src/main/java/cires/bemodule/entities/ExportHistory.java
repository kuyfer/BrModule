package cires.bemodule.entities;

import cires.bemodule.enums.ExportFormat;
import cires.bemodule.enums.ExportStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "export_history")
public class ExportHistory extends AuditableEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportStatus exportStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportFormat exportFormat;

}
