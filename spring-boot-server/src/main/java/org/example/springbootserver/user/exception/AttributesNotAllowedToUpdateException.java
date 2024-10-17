package org.example.springbootserver.user.exception;

public class AttributesNotAllowedToUpdateException extends RuntimeException {
    public AttributesNotAllowedToUpdateException() {
        super("Updating socialUserIdentifier, email, role, or walletAddress is not allowed.");
    }
}
