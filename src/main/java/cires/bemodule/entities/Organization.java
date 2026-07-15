package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.util.HashSet;
import java.util.Set;

@Builder
@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "organizations")
public class Organization extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "organization")
    private Set<Subsidiary> subsidiaries = new HashSet<>();

    private String address;
    private String contactEmail;
    private String phone;
}