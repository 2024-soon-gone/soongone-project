package org.example.springbootserver.global.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponseDTO<T> {
    private String status;
    private int statusCode;
    private String message;
    private T data;
    private Instant timestamp;
}
