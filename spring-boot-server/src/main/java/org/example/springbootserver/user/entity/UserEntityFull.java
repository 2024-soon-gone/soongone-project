package org.example.springbootserver.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
@Builder
@Entity
public class UserEntityFull {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String socialUserIdentifier;

    private String accountId;

    private String password;

    private String name;

    @NonNull
    private String email;

//    private String phone;

    private String walletAddress;

    private Date birthDay;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String introduce;

    private String profileImg;

}
