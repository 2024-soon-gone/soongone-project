package org.example.springbootserver.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.springbootserver.user.entity.BaseEntity;
import org.example.springbootserver.user.entity.UserEntity;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@Entity
@Data
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String nftAddress;

    @NonNull
    private Long nftId;

    private String text;

    // After first set and never changed
    @ManyToOne
    private UserEntity genUserId;
//    private Long genUserId;

    private String location;

    private Long likes;

    private Long comments;

    @ManyToOne
    private UserEntity ownerUserId;
//    private Long ownerUserId;
}
