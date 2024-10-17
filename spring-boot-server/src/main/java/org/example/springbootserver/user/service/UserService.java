package org.example.springbootserver.user.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.user.dto.UserDTO;
import org.example.springbootserver.user.entity.Gender;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.exception.AttributesNotAllowedToUpdateException;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Method to retrieve user information
    public UserDTO getUserInfo(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return UserDTO.from(userEntity);
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
