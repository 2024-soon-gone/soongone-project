package org.example.springbootserver.user.repository;

import org.example.springbootserver.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findBySocialUserIdentifier(String socialUserIdentifier);
//    UserEntity findById(long id);
    UserEntity findByWalletAddress(String walletAddress);
}
