package com.blog.framework.service;

import com.blog.framework.dto.UserDTO;
import com.blog.framework.entity.UserEntity;
import com.blog.framework.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String createUser(UserDTO  userDTO){
        try {
            userRepository.findByEmail(userDTO.getEmail()).ifPresent(m -> {
                throw new IllegalArgumentException("이미 가입된 유저입니다.");
            });

            //비밀번호 암호화
            String encodepwd = passwordEncoder.encode( userDTO.getPassword());

            UserEntity savedUser = new UserEntity();
            savedUser.setEmail(userDTO.getEmail());
            savedUser.setUserName(userDTO.getUsername());
            savedUser.setPassword(encodepwd);
            //가입된 유저의 이름 반환
            return userRepository.save(savedUser).getUsername();
        } catch (IllegalArgumentException e) {
            throw e;
        }

    }
}
