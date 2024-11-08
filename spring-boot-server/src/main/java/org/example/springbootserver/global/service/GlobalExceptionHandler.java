package org.example.springbootserver.global.service;

import org.example.springbootserver.global.dto.ErrorResponse;
import org.example.springbootserver.user.exception.AttributesNotAllowedToUpdateException;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String WRONG_USER_EXCEPTION_MESSAGE = "User does not exist";
    private static final String WRONG_REQUEST_EXCEPTION_MESSAGE = "Wrong Request";
    private static final String MAX_UPLOAD_SIZE_EXCEEDED_MESSAGE = "Upload size exceeded (Limit 10MB)";

    // Handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity(ErrorResponse.builder().message(ex.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

    // Handle AttributesNotAllowedToUpdateException
    @ExceptionHandler(AttributesNotAllowedToUpdateException.class)
    public ResponseEntity<ErrorResponse> handleAttributesNotAllowedToUpdateException(AttributesNotAllowedToUpdateException ex) {
        return new ResponseEntity(ErrorResponse.builder().message(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
}
