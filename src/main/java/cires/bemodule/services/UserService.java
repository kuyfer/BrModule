package cires.bemodule.services;

import cires.bemodule.dtos.RegisterRequest;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User registerUser(RegisterRequest request) {
        logger.info("registerUser");
        if (userRepository.findByUsername(request.getUsername()) != null)
            throw new RuntimeException("Username already exists");

        if (userRepository.findByEmail(request.getEmail()) != null)
            throw new RuntimeException("Email already exists");

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setAccountStatus(AccountStatus.ACTIVE);

        return userRepository.save(user);
    }
}
