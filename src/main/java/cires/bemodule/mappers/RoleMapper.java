package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateRoleRequest;
import cires.bemodule.dtos.requests.PatchRoleRequest;
import cires.bemodule.dtos.views.RoleDTO;
import cires.bemodule.entities.Role;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", source = "permissions")
    RoleDTO toRoleDto(Role role);

    Role toRole(CreateRoleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchRoleFromRequest(PatchRoleRequest request, @MappingTarget Role role);
}