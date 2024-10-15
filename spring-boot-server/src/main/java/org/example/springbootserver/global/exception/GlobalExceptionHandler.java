package org.example.springbootserver.global.exception;

import org.example.springbootserver.global.dto.ErrorResponse;
import org.example.springbootserver.user.exception.AttributesNotAllowedToUpdateException;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        // Create a custom error response
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        // Return the response with NOT_FOUND (404) status
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle AttributesNotAllowedToUpdateException
    @ExceptionHandler(AttributesNotAllowedToUpdateException.class)
    public ResponseEntity<ErrorResponse> handleAttributesNotAllowedToUpdateException(AttributesNotAllowedToUpdateException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
