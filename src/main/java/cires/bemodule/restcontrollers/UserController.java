package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public String getAllUsers() {
        return "All users";
    }

    @PostMapping
    public void createUser(@RequestBody Object user) {}

    @PutMapping("/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody Object user) {}

    @PatchMapping("/{id}")
    public void patchUser(@PathVariable Long id) {}
}