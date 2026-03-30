package cires.bemodule.entities;

//import cires.bemodule.listeners.CustomRevisionListener;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionMapping;
import jakarta.persistence.EntityListeners;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
//@RevisionEntity(CustomRevisionListener.class)
public class CustomRevisionEntity extends RevisionMapping {

    private String remoteHost;
    private String remoteUser;

}