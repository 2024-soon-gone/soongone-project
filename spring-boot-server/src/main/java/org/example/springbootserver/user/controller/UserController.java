package org.example.springbootserver.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.user.dto.UserDTO;
import org.example.springbootserver.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // GET controller to retrieve user information
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserInfo(id);
        return ResponseEntity.ok(userDTO);
    }

    // POST controller to update user information
    @PostMapping("/updateInfo")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUserInfo(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

}
