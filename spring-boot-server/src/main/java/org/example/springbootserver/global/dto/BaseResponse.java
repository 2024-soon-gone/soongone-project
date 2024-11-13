package org.example.springbootserver.global.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BaseResponse {
    protected String message;
//    protected T code;
}
