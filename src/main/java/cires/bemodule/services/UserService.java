package cires.bemodule.services;

import cires.bemodule.dtos.requests.RegisterRequest;
import cires.bemodule.dtos.views.UserDTO;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;

    // ################################# CREATE ######################################

    public User registerUser(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role adminRole = roleRepository.findByroleName(RoleType.SUPER_ADMIN)
                .orElseThrow(() -> {
                    return new RuntimeException("Role not found");
                });
        user.setRoles(List.of(adminRole));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAccountStatus(AccountStatus.ACTIVE);

        notificationService.sendRegistrationEmail(request);

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    // ################################# READ ######################################

    public UserDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new UserNotFoundException(id);
        });
        UserDTO dto = userMapper.toUserDto(user);
        return dto;
    }

    public Page<UserDTO> findAll(String role, AccountStatus status, Pageable pageable) {

        Specification<User> spec = Specification
                .where(UserSpecifications.hasRole(role))
                .and(UserSpecifications.hasStatus(status));

        Page<User> userPage = userRepository.findAll(spec, pageable);

        return userPage.map(userMapper::toUserDto);
    }

    public List<UserDTO> findAll(String role, AccountStatus status) {
        Page<UserDTO> page = findAll(role, status, Pageable.unpaged());
        return page.getContent();
    }

    // ################################# UPDATE ######################################

    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> {
            return new UserNotFoundException(id);
        });
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        User updated = userRepository.save(existingUser);
        return updated;
    }

    // ################################# DELETE ######################################

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new UserNotFoundException(id);
        });
        userRepository.delete(user);
    }

    // ################################# UTILS ######################################

    public void activateAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new UserNotFoundException(id);
        });
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
    }

    public void deactivateAccount(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            return new UserNotFoundException(id);
        });
        user.setAccountStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
    }
}