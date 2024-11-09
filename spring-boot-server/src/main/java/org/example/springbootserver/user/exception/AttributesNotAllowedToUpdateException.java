package org.example.springbootserver.user.exception;

public class AttributesNotAllowedToUpdateException extends RuntimeException {
    public AttributesNotAllowedToUpdateException(String message) {
        super(message);
    }
}
