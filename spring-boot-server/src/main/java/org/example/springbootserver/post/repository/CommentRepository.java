package org.example.springbootserver.post.repository;

import org.example.springbootserver.post.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
