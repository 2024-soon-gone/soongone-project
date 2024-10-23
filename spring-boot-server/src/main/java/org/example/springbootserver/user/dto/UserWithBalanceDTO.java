package org.example.springbootserver.user.dto;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserWithBalanceDTO {
    private UserDTO user; // UserDTO 객체
    private Long balance;  // 잔액

    public static UserWithBalanceDTO from(UserDTO userDTO, Long balance) {
        return UserWithBalanceDTO.builder()
                .user(userDTO)
                .balance(balance)
                .build();
    }
}