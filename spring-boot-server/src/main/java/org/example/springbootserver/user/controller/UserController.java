package org.example.springbootserver.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.user.dto.UserDTO;
import org.example.springbootserver.user.dto.UserWithBalanceDTO;
import org.example.springbootserver.user.entity.UserEntity;
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

    // GET controller to retrieve user information
    @GetMapping("/myinfo")
    public ResponseEntity<UserWithBalanceDTO> getCurUserInfoWithBalance() {
        UserEntity curUser = userService.getCurrentUserEntity();
        UserWithBalanceDTO userWithBalanceDTO = userService.getUserWithBalance(curUser);
        return ResponseEntity.ok(userWithBalanceDTO);
    }

    // POST controller to update user information
    @PostMapping("/updateInfo")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUserInfo(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

}
