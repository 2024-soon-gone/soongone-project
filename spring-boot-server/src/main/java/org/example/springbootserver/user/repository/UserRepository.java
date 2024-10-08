package org.example.springbootserver.user.repository;

import org.example.springbootserver.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    UserEntity findBySocialUserIdentifier(String socialUserIdentifier);
    UserEntity findById(long id);
}
