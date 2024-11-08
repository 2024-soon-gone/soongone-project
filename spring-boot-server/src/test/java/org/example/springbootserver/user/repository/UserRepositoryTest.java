package org.example.springbootserver.user.repository;

import org.example.springbootserver.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userCreateTest(){
        UserEntity user = new UserEntity();
        user.setSocialUserIdentifier("test 1111");
        user.setName("testName");
        user.setEmail("test@test.com");
        user.setRole("TEST_ROLE");
        userRepository.save(user);
//        System.out.println(ConsoleColor.CYAN);
//        System.out.println(userRepository.findAll()); System.out.println(ConsoleColor.RESET);
        System.out.println(userRepository.findAll());
    }

}