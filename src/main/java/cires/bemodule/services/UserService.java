package cires.bemodule.services;

import cires.bemodule.dtos.RegisterRequest;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.enums.RoleType;
import cires.bemodule.exceptions.UserNotFoundException;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.repositories.RoleRepository;
import cires.bemodule.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    //private final ApplicationEventPublisher publisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    public User registerUser(RegisterRequest request) {
        logger.info("registerUser");
        if (userRepository.findByUsername(request.getUsername()) != null)
            throw new RuntimeException("Username already exists");

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role adminRole = roleRepository.findByroleName(RoleType.SUPER_ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(List.of(adminRole));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAccountStatus(AccountStatus.ACTIVE);
       // publisher.publishEvent(new UserRegisteredEvent(this, user.getEmail()));
        return userRepository.save(user);
    }

    public UserDTO getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow( () -> new UserNotFoundException(id));
        return userMapper.toUserDto(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public void activateAccount(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    public void deactivateAccount(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        user.setAccountStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
