package org.example.springbootserver.global.util;

import org.example.springbootserver.global.dto.HttpResponseDTOv2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

    public static <T> HttpResponseDTOv2<T> createHttpResponse(String message, T data) {
        return new HttpResponseDTOv2<>(message, data);
    }

    public static <T> ResponseEntity<HttpResponseDTOv2<T>> mapExternalResponse(ResponseEntity<HttpResponseDTOv2> externalResponse, HttpStatus status) {
        if (externalResponse.getBody() != null) {
            HttpResponseDTOv2 externalBody = externalResponse.getBody();

            // Construct a new HttpResponseDTOv2 with the message and response data
            HttpResponseDTOv2<T> mappedResponse = new HttpResponseDTOv2<>(
                    externalBody.getMessage(),
                    (T) externalBody.getResponse()
            );

            // Return it wrapped in a ResponseEntity with the provided status
            return ResponseEntity.status(status).body(mappedResponse);
        } else {
            // Handle the case where the external response body is null
            HttpResponseDTOv2<T> errorResponse = new HttpResponseDTOv2<>("Failed to retrieve data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
