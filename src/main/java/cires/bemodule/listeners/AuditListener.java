package cires.bemodule.listeners;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;



@Data
public class AuditListener {

        private String operation;

        private long timestamp;

        // standard setters and getters for the new properties

        @PrePersist
        public void onPrePersist() {
            audit("INSERT");
        }

        @PreUpdate
        public void onPreUpdate() {
            audit("UPDATE");
        }

        @PreRemove
        public void onPreRemove() {
            audit("DELETE");
        }

        private void audit(String operation) {
            setOperation(operation);
            setTimestamp((new Date()).getTime());
        }
    }

