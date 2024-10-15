package org.example.springbootserver.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.user.dto.UserDTO;
import org.example.springbootserver.user.entity.Gender;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.exception.AttributesNotAllowedToUpdateException;
import org.example.springbootserver.user.exception.UserNotFoundException;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    // GET controller to retrieve user information
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long id) {
        // Fetch the user from the repository
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Convert the UserEntity to UserDTO
        UserDTO userDTO = UserDTO.from(userEntity);

        // Return the user information as a JSON response
        return ResponseEntity.ok(userDTO);
    }

    // POST controller to update user information
    @PostMapping("/{id}")
    public ResponseEntity<UserDTO> updateUserInfo(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // Check if any forbidden fields are being updated
        if (userDTO.getSocialUserIdentifier() != null || userDTO.getEmail() != null ||
                userDTO.getRole() != null || userDTO.getWalletAddress() != null) {
            throw new AttributesNotAllowedToUpdateException();
        }

        // Fetch the existing user from the repository
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        // Update only the allowed fields
        existingUser.setName(userDTO.getName());
        existingUser.setBirthDay(userDTO.getBirthDay());
        existingUser.setGender(userDTO.getGender() != null ? Gender.valueOf(userDTO.getGender()) : null);
        existingUser.setIntroduce(userDTO.getIntroduce());
        existingUser.setProfileImg(userDTO.getProfileImg());

        // Save the updated user back to the database
        userRepository.save(existingUser);

        // Convert the updated UserEntity back to UserDTO
        UserDTO updatedUserDTO = UserDTO.from(existingUser);

        // Return the updated user information
        return ResponseEntity.ok(updatedUserDTO);
    }

}
