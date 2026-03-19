package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import java.util.HashSet;
import java.util.Set;

@Audited @Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "organizations")
public class Organization {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "organization")
    private Set<Subsidiary> subsidiaries = new HashSet<>();

}

