package org.example.springbootserver.post.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {super(message);}
}
