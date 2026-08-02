package cires.bemodule.dtos;

import cires.bemodule.entities.CustomRevisionEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RevisionDetail {
    private CustomRevisionEntity revisionInfo;
    @JsonIgnore
    private Object entity;
    private RevisionType revisionType;
    private String entityType;
    private Long entityId;
}