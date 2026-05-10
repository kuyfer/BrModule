package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.User;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static cires.bemodule.specifications.UserSpecifications.isAdmin;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserRepository userRepository,
                          UserMapper userMapper) {this.userService = userService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        // Correct usage: call the static method
        List<User> adminUsers = userRepository.findAll(isAdmin());
        // Convert entities to DTOs (use your mapper)
        List<UserDTO> adminDTOs = adminUsers.stream().map(userMapper::toUserDto)  // or inject UserMapper
                .toList();
        return ResponseEntity.ok(adminDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public void createUser(@RequestBody Object user) {

    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Object user) {}

    @PatchMapping("/{id}")
    public void patchUser(@PathVariable Long id) {}
}