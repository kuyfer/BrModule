package cires.bemodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the BeModule application.
 * <p>
 * This class bootstraps the Spring Boot application by calling
 * {@link SpringApplication#run(Class, String...)}. It is annotated with
 * {@link SpringBootApplication} to enable auto‑configuration, component
 * scanning, and other Spring Boot defaults.
 * </p>
 *
 * @author Mhimer El Mehdi
 * @since 1.0
 * @version 1.0
 */
@SpringBootApplication
public class BeModuleApplication {

    /**
     * Starts the application.
     *
     * @param args command‑line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(BeModuleApplication.class, args);
    }

}