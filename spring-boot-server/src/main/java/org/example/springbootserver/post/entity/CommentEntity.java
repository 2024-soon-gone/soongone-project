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
public class CommentEntity extends BaseEntity {
    @Id
    @Column(name="commentId") // Custom Column Name set for UserCommentLike
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String textContent;

    @NonNull
    @ManyToOne
    private UserEntity userId;
    //    private Long userId;

    @NonNull
    @ManyToOne
    private PostEntity postId;
//    private Long postId;

    @OneToOne
    private CommentEntity parentCommentId;
//    private Long parentCommentId;
}
