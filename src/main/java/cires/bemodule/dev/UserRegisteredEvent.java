package cires.bemodule.dev;

import cires.bemodule.services.UserService;

public record UserRegisteredEvent(UserService userService, String email) {
}
