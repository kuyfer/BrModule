package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.User} was not found.
 */
public class UserNotFoundException extends EntityNotFoundException{

        public UserNotFoundException(Long id) {
            super("User not found with id: " + id);
        }

        public UserNotFoundException(String message) { super(message); }

        public UserNotFoundException(String message, Throwable cause) { super(message, cause); }
}