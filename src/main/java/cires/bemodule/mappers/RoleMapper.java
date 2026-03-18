package cires.bemodule.mappers;

import cires.bemodule.dtos.RoleDTO;
import cires.bemodule.entities.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    private final ModelMapper modelMapper;

    public RoleMapper( ModelMapper modelMapper){this.modelMapper = modelMapper;}

    public RoleDTO convertRoleToRoleDto(Role role){return modelMapper.map(role, RoleDTO.class);}

    public Role convertRoleDtoToRole(RoleDTO roleDto){return modelMapper.map(roleDto, Role.class);}

}
