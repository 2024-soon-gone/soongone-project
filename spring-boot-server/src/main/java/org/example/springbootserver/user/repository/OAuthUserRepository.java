package org.example.springbootserver.user.repository;

import org.example.springbootserver.user.entity.OAuthUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthUserRepository extends JpaRepository<OAuthUserEntity, Long> {
    OAuthUserEntity findByUsername(String username);
}
