package org.example.springbootserver.post.repository;

import org.example.springbootserver.post.entity.UserCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLike, Long> {
}
