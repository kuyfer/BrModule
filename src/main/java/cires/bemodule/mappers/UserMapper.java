package cires.bemodule.mappers;

import cires.bemodule.dtos.views.UserDTO;
import cires.bemodule.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toUserDto(User user);

    @Mapping(ignore = true, target = "password")
    User toUser(UserDTO userDTO);

}
