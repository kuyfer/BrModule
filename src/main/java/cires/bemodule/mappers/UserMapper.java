package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.PatchUserRequest;
import cires.bemodule.dtos.views.UserDTO;
import cires.bemodule.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(User user);

    @Mapping(ignore = true, target = "password")
    User toUser(UserDTO userDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchUserFromRequest(PatchUserRequest request, @MappingTarget User user);
}