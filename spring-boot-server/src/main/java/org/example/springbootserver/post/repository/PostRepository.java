package org.example.springbootserver.post.repository;

import org.example.springbootserver.post.entity.PostEntity;
import org.example.springbootserver.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Optional<PostEntity> findTopByGenUserEntityOrderByCreatedAtDesc(UserEntity currentUser);
}
