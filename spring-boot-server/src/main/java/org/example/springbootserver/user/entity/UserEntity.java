package org.example.springbootserver.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.springbootserver.post.entity.CommentEntity;
import org.example.springbootserver.post.entity.PostEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@Builder
@Entity
@Data
public class UserEntity extends BaseEntity{
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

    private String walletAddress;

    private String walletPrivateKey;

    private Date birthDay;

    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    private String introduce;

    private String profileImg;

//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<PostEntity> postsCreated = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<PostEntity> postsOwned = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<CommentEntity> commentsCreated = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<UserEntity> usersFollowing = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<UserEntity> usersFollowers = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<UserEntity> postsLiked = new ArrayList<>();
//
//    @OneToMany
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private List<UserEntity> commentsLiked = new ArrayList<>();
}

;