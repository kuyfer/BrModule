package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Audited @Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "dashboard")
public class Dashboard {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
