package cires.bemodule.services;

import cires.bemodule.dtos.requests.RegisterRequest;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.enums.RoleType;
import cires.bemodule.exceptions.validationexceptions.EmailAlreadyExistsException;
import cires.bemodule.exceptions.controllerexceptions.UserNotFoundException;
import cires.bemodule.exceptions.validationexceptions.UsernameAlreadyExistsException;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.repositories.RoleRepository;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.specifications.UserSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RoleRepository roleRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.notificationService = notificationService;
    }

    // ################################# CREATE ######################################

    public User registerUser(RegisterRequest request) {
        logger.info("Registering new user with username: {}, email: {}", request.getUsername(), request.getEmail());

        if (userRepository.findByUsername(request.getUsername()) != null) {
            logger.warn("Username already exists: {}", request.getUsername());
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role adminRole = roleRepository.findByroleName(RoleType.SUPER_ADMIN)
                .orElseThrow(() -> {
                    logger.error("Role not found: SUPER_ADMIN");
                    return new RuntimeException("Role not found");
                });
        user.setRoles(List.of(adminRole));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAccountStatus(AccountStatus.ACTIVE);

        notificationService.sendRegistrationEmail(request);

        User savedUser = userRepository.save(user);
        logger.info("User registered successfully with id: {}", savedUser.getId());
        return savedUser;
    }

    // ################################# READ ######################################

    public UserDTO findUserById(Long id) {
        logger.info("Finding user by id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found with id: {}", id);
            return new UserNotFoundException(id);
        });
        UserDTO dto = userMapper.toUserDto(user);
        logger.info("Found user with id: {}", id);
        return dto;
    }

    public List<UserDTO> findAll(String role, AccountStatus status) {
        logger.info("Finding all users with filters - role: {}, status: {}", role, status);
        Specification<User> spec = Specification
                .where(UserSpecifications.hasRole(role))
                .and(UserSpecifications.hasStatus(status));
        List<User> users = userRepository.findAll(spec);
        List<UserDTO> dtos = users.stream()
                .map(userMapper::toUserDto)
                .toList();
        logger.info("Found {} users matching filters", dtos.size());
        return dtos;
    }

    // ################################# UPDATE ######################################

    public User updateUser(Long id, User user) {
        logger.info("Updating user with id: {}", id);
        User existingUser = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found for update with id: {}", id);
            return new UserNotFoundException(id);
        });
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        User updated = userRepository.save(existingUser);
        logger.info("User updated successfully with id: {}", id);
        return updated;
    }

    // ################################# DELETE ######################################

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found for deletion with id: {}", id);
            return new UserNotFoundException(id);
        });
        userRepository.delete(user);
        logger.info("User deleted successfully with id: {}", id);
    }

    // ################################# UTILS ######################################

    public void activateAccount(Long id) {
        logger.info("Activating account for user id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found for activation with id: {}", id);
            return new UserNotFoundException(id);
        });
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        logger.info("Account activated for user id: {}", id);
    }

    public void deactivateAccount(Long id) {
        logger.info("Deactivating account for user id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.error("User not found for deactivation with id: {}", id);
            return new UserNotFoundException(id);
        });
        user.setAccountStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
        logger.info("Account deactivated for user id: {}", id);
    }
}