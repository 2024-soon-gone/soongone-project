package org.example.springbootserver.post.repository;

import org.example.springbootserver.post.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
