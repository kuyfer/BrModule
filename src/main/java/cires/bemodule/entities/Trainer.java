package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Table(name = "trainers")
public class Trainer{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialty;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "trainer_organizations",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    private Set<Organization> affiliatedOrganizations = new HashSet<>();

}