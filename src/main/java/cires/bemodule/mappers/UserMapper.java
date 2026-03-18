package cires.bemodule.mappers;

import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public UserDTO convertUserToUserDTO(User user) {return modelMapper.map(user, UserDTO.class);}

    public User convertUserDTOToUser(UserDTO userDTO) {return modelMapper.map(userDTO, User.class);}

}
