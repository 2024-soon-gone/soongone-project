package org.example.springbootserver.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${spring.baseUrl.BC_SERVER_URL}")
    private String BC_SERVER_URL;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;

    // Method to retrieve user information
    public UserDTO getUserInfo(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserDTO.from(userEntity);
    }

    public UserWithBalanceDTO getUserWithBalance(UserEntity userEntity) {
        String userAddress = userEntity.getWalletAddress();
        Long tokenBalance = this.getUserTokenBalance(userAddress); // 잔액 가져오기
        UserDTO userDTO = UserDTO.from(userEntity); // UserEntity를 UserDTO로 변환

        return UserWithBalanceDTO.from(userDTO, tokenBalance); // UserWithBalanceDTO 생성
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

    // Method to update user information
    public UserDTO updateUserInfo(UserDTO userDTO) {
        String sessionSocialUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
        // Check if any forbidden fields are being updated
        if (userDTO.getSocialUserIdentifier() != null || userDTO.getEmail() != null ||
                userDTO.getRole() != null || userDTO.getWalletAddress() != null) {
            throw new AttributesNotAllowedToUpdateException();
        }

        // Fetch the existing user from the repository
        UserEntity existingUser = userRepository.findBySocialUserIdentifier(sessionSocialUserIdentifier).orElseThrow(() -> new UserNotFoundException(sessionSocialUserIdentifier));

        // Update only the allowed fields
        existingUser.setAccountId(userDTO.getAccountId());
        existingUser.setName(userDTO.getName());
        existingUser.setBirthDay(userDTO.getBirthDay());
        existingUser.setGender(userDTO.getGender() != null ? Gender.valueOf(userDTO.getGender()) : null);
        existingUser.setIntroduce(userDTO.getIntroduce());
        existingUser.setProfileImg(userDTO.getProfileImg());

        // Save the updated user back to the database
        userRepository.save(existingUser);

        return UserDTO.from(existingUser);
    }


    public UserEntity getCurrentUserEntity() throws UserNotFoundException{
        String sessionSocialUserIdentifier = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findBySocialUserIdentifier(sessionSocialUserIdentifier)
                .orElseThrow(() -> new UserNotFoundException(sessionSocialUserIdentifier));
        return currentUser;
    }
}
