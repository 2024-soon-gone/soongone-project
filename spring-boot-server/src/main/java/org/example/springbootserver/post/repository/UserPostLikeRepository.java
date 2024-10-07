package org.example.springbootserver.post.repository;

import org.example.springbootserver.post.entity.UserPostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostLikeRepository extends JpaRepository<UserPostLike, Long> {
}
