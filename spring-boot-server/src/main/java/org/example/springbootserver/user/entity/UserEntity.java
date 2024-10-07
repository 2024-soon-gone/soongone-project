package org.example.springbootserver.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.springbootserver.customOAuth2.dto.UserDTO;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
//@Builder
@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String socialUserIdentifier;

    // Current login method is only by OAuth2. accountId and pw CAN be null
    private String accountId;

    private String password;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String role;

//    private String phone;

    private String walletAddress;

    private Date birthDay;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String introduce;

    private String profileImg;
}

;