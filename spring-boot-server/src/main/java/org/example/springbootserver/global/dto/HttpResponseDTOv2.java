package org.example.springbootserver.global.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseDTOv2<T> {
//    private String resultCode;
    private String message;
    private T response;
}
