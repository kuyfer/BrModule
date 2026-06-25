package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateRoleRequest;
import cires.bemodule.dtos.requests.PatchRoleRequest;
import cires.bemodule.dtos.views.PermissionDTO;
import cires.bemodule.dtos.views.RoleDTO;
import cires.bemodule.entities.Permission;
import cires.bemodule.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "toSummary")
    RoleDTO toDto(Role role);

    Role toEntity(CreateRoleRequest request);

    void updateEntity(PatchRoleRequest request, @MappingTarget Role role);

    @Named("toSummary")
    default Set<PermissionDTO> toSummary(Set<Permission> permissions) {
        if (permissions == null) return Set.of();
        return permissions.stream()
                .map(p -> PermissionDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .resource(p.getResource())
                        .action(p.getAction())
                        .build())
                .collect(Collectors.toSet());
    }
}