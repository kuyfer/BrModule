package cires.bemodule.listeners;

import cires.bemodule.entities.CustomRevisionEntity;
import cires.bemodule.utilities.CurrentUser;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomRevisionEntityListener implements RevisionListener {

    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity =
                (CustomRevisionEntity) revisionEntity;

        customRevisionEntity.setUsername(
                CurrentUser.INSTANCE.get()
        );
    }
}