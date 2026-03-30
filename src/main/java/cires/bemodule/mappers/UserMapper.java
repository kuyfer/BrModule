package cires.bemodule.mappers;


import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "accountStatus", target = "accountStatus")
    @Mapping(source = "roles", target = "roles")
    UserDTO toUserDto(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "accountStatus", target = "accountStatus")
    @Mapping(source = "roles", target = "roles")
    User toUser(UserDTO userDTO);

}
