package org.example.springbootserver.customOAuth2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token2OAuthDTO {
    private String role;
    private String name;
    private String socialUserIdentifier;
}