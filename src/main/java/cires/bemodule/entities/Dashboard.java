package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "dashboard")
public class Dashboard extends AuditableEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
