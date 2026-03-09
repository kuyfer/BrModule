package cires.bemodule.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "trainers")
public class Trainer{

    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String specialty;
    private String bio;

    // getters and setters
}