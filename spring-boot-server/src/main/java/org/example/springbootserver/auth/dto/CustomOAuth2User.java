package org.example.springbootserver.auth.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final Token2OAuthDTO token2OAuthDTO;

    public CustomOAuth2User(Token2OAuthDTO token2OAuthDTO) {

        this.token2OAuthDTO = token2OAuthDTO;
    }

    @Override
    public Map<String, Object> getAttributes() {

        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return token2OAuthDTO.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getName() {

        return token2OAuthDTO.getName();
    }

    public String getSocialUserIdentifier() {

        return token2OAuthDTO.getSocialUserIdentifier();
    }

}
