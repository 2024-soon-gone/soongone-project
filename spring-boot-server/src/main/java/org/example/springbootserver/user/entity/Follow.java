package org.example.springbootserver.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.springbootserver.customOAuth2.dto.UserDTO;
import org.example.springbootserver.user.entity.BaseEntity;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
//@EqualsAndHashCode(callSuper = true)
//@Builder
@Entity
@Data
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    private UserEntity followSrcId;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    private UserEntity followDstId;
}
