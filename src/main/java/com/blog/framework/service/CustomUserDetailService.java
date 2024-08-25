package com.blog.framework.service;

import com.blog.framework.entity.UserEntity;
import com.blog.framework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);


        return optionalUserEntity.map(userEntity -> {
            // 권한 설정
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            // UserDetails 객체 생성
            return new User(
                    userEntity.getEmail(), // 사용자 이름으로 이메일 사용
                    userEntity.getPassword(), // 암호화된 비밀번호
                    authorities
            );
        }).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
