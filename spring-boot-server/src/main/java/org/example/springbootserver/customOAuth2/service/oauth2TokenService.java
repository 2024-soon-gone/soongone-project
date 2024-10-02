package org.example.springbootserver.customOAuth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.customOAuth2.dto.*;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.example.springbootserver.jwt.JWTUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class oauth2TokenService {
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public CustomOAuth2User verifyAccessToken(String provider, String accessToken) throws OAuth2AuthenticationException {

        if(provider == null){
            System.out.println("Provider is null");
            OAuth2AuthenticationException providerIsNull = new OAuth2AuthenticationException("Provider is null");
            throw providerIsNull;
        }
        String url;

        if(provider.equals("google")){
            // Define the Google API URL with the access token
            url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token=" + accessToken;
        } else {
            System.out.println("Provider is not Supported");
            OAuth2AuthenticationException providerNotSupported = new OAuth2AuthenticationException("Provider is null");
            throw providerNotSupported;
        }
        // Use RestTemplate to send a GET request to the Google API
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> tokenVerifiedResponse = restTemplate.getForEntity(url, String.class);
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
        // Current Manual Access Token Verification only available for Google
         if (provider.equals("google")) {

            oAuth2Response = new GoogleResponse(googleUserAttributes);
        }
        else {

            return null;
        }

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        // UserName(Unique한 식별자)을 통해 사용자의 정보 존재 여부를 Jpa를 통해 확인한다.
        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) { // 기존 유저가 존재하지 않는다면

            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
        else{ // 기존 유저가 존재한다면
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            // 소셜 프로필 정보가 변경됐다면 해당 정보를 update
            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);
        }
    }

    public void onTokenVerificationSuccess(HttpServletResponse response, CustomOAuth2User customOAuth2User) throws IOException {
        String username = customOAuth2User.getUsername();
        String jwtToken = jwtUtil.createJwt(username, "ROLE_USER", 60*60*60L); // 3번째 인자는 JWT의 수명

        response.addCookie(createCookie("Authorization", jwtToken));

        // No need for redirection when handling Mobile App redirection
//        response.sendRedirect("http://localhost:3000/"); => 3000번 안열어놨기에 아래의 8080으로 세팅
//        response.sendRedirect("http://localhost:8080/");
//        return response;
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true); => https에서만 사용할 수 있도록 해주는 코드. 현재는 Local http 환경에서 진행하기에 주석처리
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
