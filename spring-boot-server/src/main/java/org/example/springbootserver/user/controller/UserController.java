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

    @GetMapping("/{id}")
    public ResponseEntity<UserWithBalanceDTO> getUserWithBalance(@PathVariable Long id) {
        UserWithBalanceDTO userWithBalanceDTO = userService.getUserWithBalance(id);
        return ResponseEntity.ok(userWithBalanceDTO);
    }

    @GetMapping("/myinfo")
    public ResponseEntity<UserWithBalanceDTO> getCurUserInfoWithBalance() {
        UserWithBalanceDTO userWithBalanceDTO = userService.getUserWithBalance();
        return ResponseEntity.ok(userWithBalanceDTO);
    }

    @PostMapping("/updateInfo")
    public ResponseEntity<UserDTO> updateUserInfo(@RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUserInfo(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }

}
