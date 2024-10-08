package org.example.springbootserver.customOAuth2.service;


import lombok.RequiredArgsConstructor;
import org.example.springbootserver.customOAuth2.dto.*;
import org.example.springbootserver.user.entity.OAuthUserEntity;
import org.example.springbootserver.user.repository.OAuthUserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthUserRepository oAuthUserRepository;

//    public CustomOAuth2UserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 요청이 Naver인지 Google인지를 확인하는
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

//        System.out.println("OAUTH2 Response :  " + oAuth2User.getAttributes());

        //리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        // UserName(Unique한 식별자)을 통해 사용자의 정보 존재 여부를 Jpa를 통해 확인한다.
//        UserEntity existData = userRepository.findByUsername(username);
        OAuthUserEntity existData = oAuthUserRepository.findByUsername(username);

        if (existData == null) { // 기존 유저가 존재하지 않는다면

            OAuthUserEntity OAuthUserEntity = new OAuthUserEntity();
            OAuthUserEntity.setUsername(username);
            OAuthUserEntity.setEmail(oAuth2Response.getEmail());
            OAuthUserEntity.setName(oAuth2Response.getName());
            OAuthUserEntity.setRole("ROLE_USER");

//            userRepository.save(OAuthUserEntity);
            oAuthUserRepository.save(OAuthUserEntity);

            Token2OAuthDTO token2OAuthDTO = new Token2OAuthDTO();
            token2OAuthDTO.setSocialUserIdentifier(username);
            token2OAuthDTO.setName(oAuth2Response.getName());
            token2OAuthDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(token2OAuthDTO);
        }
        else{ // 기존 유저가 존재한다면
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

//            userRepository.save(existData);
            oAuthUserRepository.save(existData);

            Token2OAuthDTO token2OAuthDTO = new Token2OAuthDTO();
            token2OAuthDTO.setSocialUserIdentifier(existData.getUsername());
            token2OAuthDTO.setName(oAuth2Response.getName());
            token2OAuthDTO.setRole(existData.getRole());

            return new CustomOAuth2User(token2OAuthDTO);
        }
    }
}
