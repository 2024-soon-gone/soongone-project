package org.example.springbootserver.post.entity;

import org.example.springbootserver.user.entity.BaseEntity;


import jakarta.persistence.*;
import lombok.*;

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

//    @NonNull
//    @ManyToOne
//    private PostEntity postEntity;
    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)  // Set cascade type
    private PostEntity postEntity;
}
