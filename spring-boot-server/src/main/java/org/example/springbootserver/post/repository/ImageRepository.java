package org.example.springbootserver.post.repository;

import org.example.springbootserver.post.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    Optional<ImageEntity> findByPostEntity_Id(Long postEntityId);
}
