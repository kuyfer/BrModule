package cires.bemodule.entities;

import cires.bemodule.listeners.CustomRevisionEntityListener;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionMapping;


@Data @NoArgsConstructor @AllArgsConstructor
@Entity(name = "CustomRevisionEntity")
@Table(name = "CUSTOM_REV_INFO")
@RevisionEntity(CustomRevisionEntityListener.class)
public class CustomRevisionEntity extends RevisionMapping {

    private String username;
    private String ipAddress;

}