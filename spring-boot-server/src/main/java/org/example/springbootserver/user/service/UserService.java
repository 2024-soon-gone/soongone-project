package org.example.springbootserver.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.springbootserver.auth.service.UserDetailsServiceImpl;
import org.example.springbootserver.user.dto.UserDTO;
import org.example.springbootserver.user.dto.UserWithBalanceDTO;
import org.example.springbootserver.user.entity.Gender;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.exception.AttributesNotAllowedToUpdateException;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public UserDTO getUserInfo(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserDTO.from(userEntity);
    }

    public UserWithBalanceDTO getUserWithBalance() {
        UserEntity curUserEntity = userDetailsServiceImpl.getUserEntityByContextHolder();
        Long tokenBalance = this.getUserTokenBalance(curUserEntity.getWalletAddress());
        UserDTO userDTO = UserDTO.from(curUserEntity);
        return UserWithBalanceDTO.from(userDTO, tokenBalance);
    }

    public UserWithBalanceDTO getUserWithBalance(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Long tokenBalance = this.getUserTokenBalance(userEntity.getWalletAddress());
        UserDTO userDTO = UserDTO.from(userEntity);
        return UserWithBalanceDTO.from(userDTO, tokenBalance);
    }

    public Long getUserTokenBalance(String userAddress) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(BC_SERVER_URL)
                    .path("/onchain/token-balance/" + userAddress)
                    .encode()
                    .build()
                    .toUri();

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), HashMap.class);
            String tokenBalanceString = (String) ((Map<String, Object>) responseMap.get("data")).get("balance");
            Long tokenBalance = Long.valueOf(tokenBalanceString);

            return tokenBalance;
        } catch (RestClientException e) {
            throw new RuntimeException("Blockchain Http Request Failed");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Extracting balance from Http Response failed");
        } catch (Exception e) {
            throw new RuntimeException("Unknown Error");
        }
    }

    public UserDTO updateUserInfo(UserDTO userDTO) {
        String sessionSocialUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();

        List<String> disallowedAttributes = new ArrayList<>();
        if (userDTO.getSocialUserIdentifier() != null) {
            disallowedAttributes.add("socialUserIdentifier");
        }
        if (userDTO.getEmail() != null) {
            disallowedAttributes.add("email");
        }
        if (userDTO.getRole() != null) {
            disallowedAttributes.add("role");
        }
        if (userDTO.getWalletAddress() != null) {
            disallowedAttributes.add("walletAddress");
        }

        if (!disallowedAttributes.isEmpty()) {
            String message = "Updating the following attributes is not allowed: " + String.join(", ", disallowedAttributes);
            throw new AttributesNotAllowedToUpdateException(message);
        }

        UserEntity existingUser = userRepository.findBySocialUserIdentifier(sessionSocialUserIdentifier)
                .orElseThrow(() -> new UserNotFoundException(sessionSocialUserIdentifier));
        existingUser.setAccountId(userDTO.getAccountId());
        existingUser.setName(userDTO.getName());
        existingUser.setBirthDay(userDTO.getBirthDay());
        existingUser.setGender(userDTO.getGender() != null ? Gender.valueOf(userDTO.getGender()) : null);
        existingUser.setIntroduce(userDTO.getIntroduce());
        existingUser.setProfileImg(userDTO.getProfileImg());

        userRepository.save(existingUser);

        return UserDTO.from(existingUser);
    }

}
