package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.UserDTO;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.mappers.UserMapper;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) AccountStatus status,
            @PageableDefault(size = 20) Pageable pageable)
        {
        Page<UserDTO> users = userService.findAll(role, status,pageable);
        return ResponseEntity.ok(users);
    }

    @PreAuthorize( "hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public void createUser(@RequestBody Object user) {

    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Object user) {}

    @PatchMapping("/{id}")
    public void patchUser(@PathVariable Long id) {}
}