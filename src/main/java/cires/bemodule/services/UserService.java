package cires.bemodule.services;

import cires.bemodule.dtos2.RegisterRequest;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.enums.RoleType;
import cires.bemodule.exceptions.validationexceptions.EmailAlreadyExistsException;
import cires.bemodule.exceptions.controllerexceptions.UserNotFoundException;
import cires.bemodule.exceptions.validationexceptions.UsernameAlreadyExistsException;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.RoleRepository;
import cires.bemodule.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final EmailQueueProducer emailQueueProducer;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleRepository roleRepository, EmailQueueProducer emailQueueProducer) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.emailQueueProducer = emailQueueProducer;
    }

    // ################################# CREATE ######################################

    public User registerUser(RegisterRequest request) {
        logger.info("registerUser");
        if (userRepository.findByUsername(request.getUsername()) != null)
            throw new UsernameAlreadyExistsException("Username already exists");

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new EmailAlreadyExistsException("Email already exists");

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

        sendRegistrationEmail(request);

        return userRepository.save(user);
    }

    // ################################# READ ######################################

    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow( () -> new UserNotFoundException(id));
        return userMapper.toUserDto(user);
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

// ################################# UPDATE ######################################

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);
    }

    // ################################# DELETE ######################################

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    // ################################# UTILS ######################################

    private void sendRegistrationEmail(RegisterRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("recipientName", request.getFirstName());
        model.put("username", request.getUsername());
        model.put("body", "Hope you are doing well.");

        EmailPayload payload = new EmailPayload(
                request.getEmail(),
                "Welcome " + request.getUsername(),
                "welcome",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.ACCOUNT_CREATION);
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


}
