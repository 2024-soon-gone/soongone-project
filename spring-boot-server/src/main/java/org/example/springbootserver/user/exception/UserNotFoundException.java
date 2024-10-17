package org.example.springbootserver.user.exception;

public class UserNotFoundException extends RuntimeException {
    // Constructor for user ID
    public UserNotFoundException(Long userId) {
        super("User not found with ID: " + userId);
    }

    // Constructor for a custom string message
    public UserNotFoundException(String message) {
        super("User not found: " + message);
    }
}
