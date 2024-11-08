package org.example.springbootserver.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootserver.auth.dto.CustomOAuth2User;
import org.example.springbootserver.user.entity.UserEntity;
import org.example.springbootserver.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String socialUserIdentifier) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findBySocialUserIdentifier(socialUserIdentifier)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
        return new User(userEntity.getSocialUserIdentifier(), userEntity.getPassword(), getAuthorities(userEntity));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity userEntity) {
        // 권한 정보를 GrantedAuthority 객체의 컬렉션으로 변환
        return Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole()));
    }

    public UserEntity getUserEntityByContextHolder(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomOAuth2User oAuth2User = (CustomOAuth2User) principal;
        return userRepository.findBySocialUserIdentifier(oAuth2User.getSocialUserIdentifier())
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    public Authentication getAuthentication() { return SecurityContextHolder.getContext().getAuthentication(); }
}
