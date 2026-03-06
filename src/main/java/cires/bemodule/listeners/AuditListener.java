package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.Data;


@Table(name = "audit")
@Data
public class AuditListener {
    @Id
    private Long id;

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyOperation(Object object){
        System.out.println("Before any operation");
        @Column(name = "operation")
        private String operation;

        @Column(name = "timestamp")
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
}
