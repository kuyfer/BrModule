package cires.bemodule.listeners;

import cires.bemodule.entities.CustomRevisionEntity;
import cires.bemodule.utilities.CurrentUser;
import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Component;

/**
 * Hibernate Envers {@link RevisionListener} that automatically populates the
 * {@link CustomRevisionEntity} with the currently authenticated user's
 * username and the client's IP address whenever a new revision is created.
 * <p>
 * The values are obtained from {@link CurrentUser#INSTANCE}, which stores
 * request‑scoped information in thread‑local variables.  This listener is
 * registered as a Spring {@link Component} so that Envers can discover it.
 * </p>
 */
@Component
public class CustomRevisionEntityListener implements RevisionListener {

    /**
     * Called by Envers each time a new revision is created.
     * <p>
     * Casts the provided revision entity to {@link CustomRevisionEntity} and
     * sets the username and IP address obtained from
     * {@link CurrentUser#INSTANCE}.
     * </p>
     *
     * @param revisionEntity the revision entity being created (must be a
     *                       {@link CustomRevisionEntity})
     */
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity =
                (CustomRevisionEntity) revisionEntity;

        customRevisionEntity.setUsername(
                CurrentUser.INSTANCE.get()
        );
        customRevisionEntity.setIpAddress(
                CurrentUser.INSTANCE.getIpAddress()
        );
    }
}