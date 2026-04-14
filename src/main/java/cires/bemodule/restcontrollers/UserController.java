package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.UserDTO;
import cires.bemodule.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {this.userService = userService;}

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUser(id);
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