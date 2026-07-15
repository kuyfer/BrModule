package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CreateUserRequest;
import cires.bemodule.dtos.requests.PatchUserRequest;
import cires.bemodule.dtos.views.UserDTO;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // ################################# CREATE ######################################

    @PostMapping
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserRequest request) {
        UserDTO createdUser = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // ################################# READ ######################################

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.findUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) AccountStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDTO> users = userService.findAll(role, status, pageable);
        return ResponseEntity.ok(users);
    }

    // ################################# UPDATE ######################################

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UserDTO> patch(@PathVariable Long id,
                                         @RequestBody @Valid PatchUserRequest request) {
        return ResponseEntity.ok(userService.patchUser(id, request));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Void> activateAccount(@PathVariable Long id) {
        userService.activateAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Void> deactivateAccount(@PathVariable Long id) {
        userService.deactivateAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Void> addRolesToUser(@PathVariable Long userId,
                                               @RequestBody Set<Long> roleIds) {
        userService.addRolesToUser(userId, roleIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Void> removeRolesFromUser(@PathVariable Long userId,
                                                    @RequestBody Set<Long> roleIds) {
        userService.removeRolesFromUser(userId, roleIds);
        return ResponseEntity.ok().build();
    }

    // ################################# DELETE ######################################

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}