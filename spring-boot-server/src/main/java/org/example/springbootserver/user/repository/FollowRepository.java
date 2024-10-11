package org.example.springbootserver.user.repository;

import org.example.springbootserver.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
