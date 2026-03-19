package cires.bemodule.entities;

//import cires.bemodule.listeners.CustomRevisionListener;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionMapping;

@Entity
@RevisionEntity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
//@EntityListeners(CustomRevisionListener.class)
public class CustomRevisionEntity extends RevisionMapping {
    private String remoteHost;
    private String remoteUser;
}
