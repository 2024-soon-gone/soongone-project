package org.example.springbootserver.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.springbootserver.user.entity.UserEntity;

import java.util.Date;

@Builder
@Getter
public class UserDTO {
    private Long id;
    private String socialUserIdentifier;
    private String accountId;
    private String name;
    private String email;
    private String role;
    private String walletAddress;
    private Date birthDay;
    private String gender; // Gender를 문자열로 변환
    private String introduce;
    private String profileImg;

    public static UserDTO from(UserEntity userEntity) {
        return UserDTO.builder()
                .id(userEntity.getId())
                .socialUserIdentifier(userEntity.getSocialUserIdentifier())
                .accountId(userEntity.getAccountId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .role(userEntity.getRole())
                .walletAddress(userEntity.getWalletAddress())
                .birthDay(userEntity.getBirthDay())
                .gender(userEntity.getGender() != null ? userEntity.getGender().name() : null) // Gender를 문자열로 변환
                .introduce(userEntity.getIntroduce())
                .profileImg(userEntity.getProfileImg())
                .build();
    }
}
