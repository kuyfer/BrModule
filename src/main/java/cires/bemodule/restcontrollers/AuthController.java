package cires.bemodule.restcontrollers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public void login(@RequestBody Object loginRequest) {}

    @PostMapping("/refresh")
    public void refresh(@RequestBody Object refreshRequest) {}

    @PostMapping("/logout")
    public void logout() {}
}