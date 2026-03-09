package cires.bemodule.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "participants")
public class Participant {


    @Id
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    private String phone;

    // optional: link to a user account if internal
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // getters and setters
}