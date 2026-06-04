package cires.bemodule.mappers;

import cires.bemodule.dtos.views.RoleDTO;
import cires.bemodule.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDTO toDto(Role role);

    Role toRole(RoleDTO roleDTO);

}
