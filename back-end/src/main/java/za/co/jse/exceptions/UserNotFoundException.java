package za.co.jse.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(String.format("User with user name %s does not exist", username));
    }
}
