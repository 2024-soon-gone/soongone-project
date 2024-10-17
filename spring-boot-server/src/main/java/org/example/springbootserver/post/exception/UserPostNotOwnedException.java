package org.example.springbootserver.post.exception;

public class UserPostNotOwnedException extends RuntimeException {
    public UserPostNotOwnedException(String message) {
        super(message);
    }
}
