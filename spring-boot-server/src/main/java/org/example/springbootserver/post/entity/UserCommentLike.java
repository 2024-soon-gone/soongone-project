package org.example.springbootserver.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.springbootserver.user.entity.BaseEntity;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@Builder
@Entity
@Data
public class UserCommentLike extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    @OneToOne
    private PostEntity postEntity;

    @NonNull
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="commentId",insertable = false, updatable = false)
    private List<CommentEntity> comments;
}

;