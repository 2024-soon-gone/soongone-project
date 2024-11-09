package org.example.springbootserver.global.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.example.springbootserver.global.dto.ErrorResponse;
import org.example.springbootserver.global.dto.HttpResponseDTO;
import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.example.springbootserver.user.exception.AttributesNotAllowedToUpdateException;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import java.time.Instant;

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

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<HttpResponseDTO<Object>> handleRestClientException(RestClientException e) {
        HttpResponseDTO<Object> errorResponse = new HttpResponseDTO<>("error", 500, "External API error" , e.getMessage(), Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<HttpResponseDTO<Object>> handleJsonProcessingException(JsonProcessingException e) {
        HttpResponseDTO<Object> errorResponse = new HttpResponseDTO<>("error", 500, "JSON parsing error: " + e.getMessage(), null, Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<HttpResponseDTO<Object>> handleJsonMappingException(JsonMappingException e) {
        HttpResponseDTO<Object> errorResponse = new HttpResponseDTO<>("error", 500, "JSON mapping error: " + e.getMessage(), null, Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
