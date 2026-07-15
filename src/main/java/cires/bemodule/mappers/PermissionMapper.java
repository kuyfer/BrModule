package cires.bemodule.mappers;

import cires.bemodule.dtos.views.PermissionDTO;
import cires.bemodule.entities.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionDTO toPermissionDto(Permission permission);
}