package cires.bemodule.services;

import cires.bemodule.dtos.RegisterRequest;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    //private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ModelMapper modelMapper;


    public User registerUser(RegisterRequest request) {
        logger.info("registerUser");
        if (userRepository.findByUsername(request.getUsername()) != null)
            throw new RuntimeException("Username already exists");

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAccountStatus(AccountStatus.PENDING);
       // publisher.publishEvent(new UserRegisteredEvent(this, user.getEmail()));
        return userRepository.save(user);
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return userMapper.convertUserToUserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream().map((element) -> modelMapper.map(element, UserDTO.class))
                .collect(Collectors.toList());
    }

    public void activateAccount(Long id){
        User user = userRepository.findById(id).orElseThrow();
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    public void deactivateAccount(Long id){
        User user = userRepository.findById(id).orElseThrow();
        user.setAccountStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
