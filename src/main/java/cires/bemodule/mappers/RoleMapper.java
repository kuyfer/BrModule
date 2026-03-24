package cires.bemodule.mappers;


import cires.bemodule.dtos.RoleDTO;
import cires.bemodule.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(source = "id", target ="id")
    @Mapping(source = "roleName", target ="roleName")
    RoleDTO toDto(Role role);

    @Mapping(source = "id", target ="id")
    @Mapping(source = "roleName", target ="roleName")
    Role toRole(RoleDTO roleDTO);

}
