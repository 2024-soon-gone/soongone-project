package org.example.springbootserver.global.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private int status;
    private String message;
}
