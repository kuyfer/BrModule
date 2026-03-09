package cires.bemodule.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class Permission{

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String resource;
    private String action;

    // getters and setters
}