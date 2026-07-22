package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateUserRequest;
import cires.bemodule.dtos.requests.PatchUserRequest;
import cires.bemodule.dtos.views.UserDTO;
import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.exceptions.notfound.RoleNotFoundException;
import cires.bemodule.exceptions.notfound.UserNotFoundException;
import cires.bemodule.exceptions.validation.DuplicateEmailException;
import cires.bemodule.exceptions.validation.DuplicateUsernameException;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.repositories.RoleRepository;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.specifications.UserSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final NotificationService notificationService;
    private final PasswordResetService passwordResetService;

// ################################# CREATE ######################################

    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.warn("Attempt to create user with duplicate username: {}", request.getUsername());
            throw new DuplicateUsernameException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Attempt to create user with duplicate email: {}", request.getEmail());
            throw new DuplicateEmailException("Email already exists");
        }

        Set<Role> roles = null;
        if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
            roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .accountStatus(AccountStatus.PENDING)
                .password(null)
                .roles(roles)
                .build();

        User saved = userRepository.save(user);
        log.info("User created successfully with id: {} and username: {}", saved.getId(), saved.getUsername());
        passwordResetService.generateAndSendPasswordSetupToken(saved);
        return userMapper.toUserDto(saved);
    }

    // ################################# READ ######################################

    public UserDTO findUserById(Long id) {
        log.debug("Finding user by id: {}", id);
        User user = getUserIdOrThrow(id);
        UserDTO dto = userMapper.toUserDto(user);
        log.debug("Found user with id: {}", id);
        return dto;
    }

    public Page<UserDTO> findAll(String role, AccountStatus status, Pageable pageable) {
        log.debug("Fetching users page - role: {}, status: {}, pageable: {}", role, status, pageable);
        Specification<User> spec = Specification
                .where(UserSpecifications.hasRole(role))
                .and(UserSpecifications.hasStatus(status));

        Page<User> userPage = userRepository.findAll(spec, pageable);
        log.debug("Found {} users (page {} of {})", userPage.getNumberOfElements(), userPage.getNumber(), userPage.getTotalPages());
        return userPage.map(userMapper::toUserDto);
    }

    public List<UserDTO> findAll(String role, AccountStatus status) {
        log.debug("Fetching all users (unpaged) - role: {}, status: {}", role, status);
        Page<UserDTO> page = findAll(role, status, Pageable.unpaged());
        return page.getContent();
    }

    // ################################# UPDATE ######################################

    public UserDTO patchUser(Long id, PatchUserRequest request) {
        log.info("Patching User id={} with request: {}", id, request);
        User user = getUserIdOrThrow(id);
        userMapper.patchUserFromRequest(request, user);
        User saved = userRepository.save(user);
        log.info("User patched id={}, email={}", saved.getId(), saved.getEmail());
        return userMapper.toUserDto(saved);
    }

    // ################################# DELETE ######################################

    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        User user = getUserIdOrThrow(id);
        userRepository.delete(user);
        log.info("User deleted successfully with id: {}", id);
    }
    // ################################# SPECIAL ####################################

    public void activateAccount(Long id) {
        log.info("Activating account for user id: {}", id);
        User user = getUserIdOrThrow(id);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        log.info("Account activated successfully for user id: {}", id);
    }

    public void deactivateAccount(Long id) {
        log.info("Deactivating account for user id: {}", id);
        User user = getUserIdOrThrow(id);
        user.setAccountStatus(AccountStatus.INACTIVE);
        userRepository.save(user);
        log.info("Account deactivated successfully for user id: {}", id);
    }

    @Transactional
    public void addRolesToUser(Long userId, Set<Long> roleIds) {
        log.info("Adding roles: {} to user id: {}", roleIds, userId);
        User user = getUserIdOrThrow(userId);
        Set<Role> roles = new HashSet<>();
        for (Long rid : roleIds) {
            Role role = roleRepository.findById(rid)
                    .orElseThrow(() -> new RoleNotFoundException(rid));
            roles.add(role);
        }
        user.getRoles().addAll(roles);
        userRepository.save(user);
        log.info("Roles added successfully to user id: {}", userId);
    }

    @Transactional
    public void removeRolesFromUser(Long userId, Set<Long> roleIds) {
        log.info("Removing roles: {} from user id: {}", roleIds, userId);
        User user = getUserIdOrThrow(userId);
        for (Long rid : roleIds) {
            Role role = roleRepository.findById(rid)
                    .orElseThrow(() -> new RoleNotFoundException(rid));
            user.getRoles().remove(role);
        }
        userRepository.save(user);
        log.info("Roles removed successfully from user id: {}", userId);
    }

    // ################################# UTILS ######################################

    private User getUserIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}