package org.example.springbootserver.post.entity;

import org.example.springbootserver.user.entity.BaseEntity;


import jakarta.persistence.*;
import lombok.*;
import org.example.springbootserver.customOAuth2.dto.UserDTO;
import org.example.springbootserver.user.entity.BaseEntity;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
//@Builder
@Entity
@Data
public class ImageEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String imgUrl;

    @NonNull
    @ManyToOne
    private PostEntity postId;
}
