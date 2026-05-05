package cires.bemodule.entities;

import cires.bemodule.listeners.CustomRevisionEntityListener;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionMapping;

@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "CustomRevisionEntity")
@Table(name = "CUSTOM_REV_INFO")
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity extends RevisionMapping {

    private String username;

    private String ipAddress;

}