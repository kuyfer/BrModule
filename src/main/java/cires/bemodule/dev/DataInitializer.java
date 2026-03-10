package cires.bemodule.dev;

import cires.bemodule.entities.User;
import cires.bemodule.enums.UserStatus;
import cires.bemodule.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() == 0) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setEmail("test@example.com");
            testUser.setStatus(UserStatus.ACTIVE); // Assuming you have this enum value
            
            User saved = userRepository.save(testUser);
            System.out.println("Created test user with ID: " + saved.getId());
        } else {
            System.out.println("Users already exist in database");
        }
    }
}
