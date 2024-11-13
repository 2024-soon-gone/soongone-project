package org.example.springbootserver.post.exception;

public class PostNotOwnedException extends RuntimeException {
    public PostNotOwnedException(String message) {
        super(message);
    }
}
