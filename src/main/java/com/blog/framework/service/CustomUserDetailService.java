package com.blog.framework.service;

import com.blog.framework.entity.UserEntity;
import com.blog.framework.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
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
            List<GrantedAuthority> authorities = new ArrayList<>();
            // 권한을 여기에 추가할 수 있습니다. 예:
            // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

            return new User(userEntity.getUsername(), userEntity.getPassword(), authorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}
