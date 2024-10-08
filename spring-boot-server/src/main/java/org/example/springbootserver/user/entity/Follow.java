package org.example.springbootserver.user.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Follow extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    //    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity followSrcId;

    @NonNull
    @ManyToOne
    //    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity followDstId;
}
