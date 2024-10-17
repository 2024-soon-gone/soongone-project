package org.example.springbootserver.customOAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.customOAuth2.dto.*;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.example.springbootserver.user.repository.OAuthUserRepository;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.springbootserver.jwt.JWTUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class oauth2TokenService {

    private final UserRepository userRepository;
    private final OAuthUserRepository oAuthUserRepository;
    private final JWTUtil jwtUtil;

    public void verifyAccessToken(String provider, String accessToken, HttpServletResponse response) throws OAuth2AuthenticationException, IOException {

        if (provider == null) {
            System.out.println("Provider is null");
            throw new OAuth2AuthenticationException("Provider is null");
        }

        String url;

        if ("google".equals(provider)) {
            url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        } else {
            throw new OAuth2AuthenticationException("Provider is not supported");
        }

        // Use RestTemplate to send a GET request to the Google API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> tokenVerifiedResponse = restTemplate.getForEntity(url, String.class);

        System.out.println("Google Token response header : " + tokenVerifiedResponse.getHeaders());
        System.out.println("Google Token Verified result : " + tokenVerifiedResponse.getBody());

        // Parse the response body (JSON) into a Map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> googleUserAttributes;
        try {
            googleUserAttributes = objectMapper.readValue(tokenVerifiedResponse.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            throw new OAuth2AuthenticationException("Failed to parse Google response to JSON");
        }

        OAuth2Response oAuth2Response = null;
        if ("google".equals(provider)) {
            oAuth2Response = new GoogleResponse(googleUserAttributes);
        } else {
            throw new OAuth2AuthenticationException("Provider is not supported");
        }

        // Create the social user identifier
        String sociaUserIdenfier = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // Attempt to find the user by social user identifier
        Optional<UserEntity> existUserOptional = userRepository.findBySocialUserIdentifier(sociaUserIdenfier);

        if (existUserOptional.isEmpty()) { // If user does not exist
            // Create a new user
            UserEntity newUser = createNewUser(oAuth2Response);
            userRepository.save(newUser);

            Token2OAuthDTO token2OAuthDTO = new Token2OAuthDTO();
            token2OAuthDTO.setSocialUserIdentifier(sociaUserIdenfier);
            token2OAuthDTO.setName(oAuth2Response.getName());
            token2OAuthDTO.setRole("ROLE_USER");

            // Handle successful token verification for a new user
            onTokenVerificationSuccess(response, new CustomOAuth2User(token2OAuthDTO), true);

        } else { // If user exists
            UserEntity existUser = existUserOptional.get(); // Retrieve the existing user

            // Update the user information
            updateUser(existUser, oAuth2Response);

            Token2OAuthDTO token2OAuthDTO = new Token2OAuthDTO();
            token2OAuthDTO.setSocialUserIdentifier(sociaUserIdenfier);
            token2OAuthDTO.setName(oAuth2Response.getName());
            token2OAuthDTO.setRole(existUser.getRole());

            // Handle successful token verification for an existing user
            onTokenVerificationSuccess(response, new CustomOAuth2User(token2OAuthDTO), false);
        }
    }


    public void onTokenVerificationSuccess(HttpServletResponse response, CustomOAuth2User customOAuth2User, boolean isFirst) throws IOException {
        String socialUserIdentifier = customOAuth2User.getSocialUserIdentifier();
        String jwtToken = jwtUtil.createJwt(socialUserIdentifier, "ROLE_USER", 1000*60*60*60L); // 3번째 인자는 JWT의 수명

        response.addCookie(createCookie("Authorization", jwtToken));

        // Manually setting jwtToken in Response Body
        // Prepare response body as JSON
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("jwtToken", jwtToken);
        responseBody.put("message", "Social Login Successful and Jwt Token created");
        responseBody.put("isFirst", isFirst);

        // Set content type and write JSON to response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        response.getOutputStream().write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
        // No need for redirection when handling Mobile App redirection
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true); => https에서만 사용할 수 있도록 해주는 코드. 현재는 Local http 환경에서 진행하기에 주석처리
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private UserEntity createNewUser(OAuth2Response oAuth2Response) {
        UserEntity userEntity = new UserEntity();
        String socialUserIdentifier =  oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
        String name = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();
        userEntity.setSocialUserIdentifier(socialUserIdentifier);
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setRole("ROLE_USER");
        return userEntity;
    }

    private void updateUser(UserEntity existData, OAuth2Response oAuth2Response) {
        existData.setEmail(oAuth2Response.getEmail());
        existData.setName(oAuth2Response.getName());
        userRepository.save(existData);
    }
}
