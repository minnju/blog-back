package com.blog.framework.service;

import com.blog.framework.dto.LoginReqDTO;
import com.blog.framework.dto.UserDTO;
import com.blog.framework.entity.UserEntity;
import com.blog.framework.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64.Decoder;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String createUser(UserDTO  userDTO){
        try {
            Decoder decoder = Base64.getDecoder();
            String email = new String(decoder.decode(userDTO.getEmail()));
            String password = new String(decoder.decode(userDTO.getPassword()));
            userRepository.findByEmail(email).ifPresent(m -> {
                throw new IllegalArgumentException("이미 가입된 유저입니다.");
            });

            //비밀번호 암호화
            String encodepwd = passwordEncoder.encode( password);

            UserEntity savedUser = new UserEntity();
            savedUser.setEmail(email);
            savedUser.setUserName(userDTO.getUsername());
            savedUser.setPassword(encodepwd);
            //가입된 유저의 이름 반환
            return userRepository.save(savedUser).getUsername();
        } catch (IllegalArgumentException e) {
            throw e;
        }

    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserEntity) {
            return (UserEntity) authentication.getPrincipal();
        }
        return null; // 인증된 사용자가 없는 경우
    }

    public UserDTO login(LoginReqDTO reqDTO){
        return new UserDTO();
    }
}
