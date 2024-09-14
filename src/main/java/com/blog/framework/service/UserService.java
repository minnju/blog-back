package com.blog.framework.service;

import com.blog.framework.dto.LoginReqDTO;
import com.blog.framework.dto.UserDTO;
import com.blog.framework.entity.UserEntity;
import com.blog.framework.repository.UserRepository;
import com.blog.framework.util.JwtTokenUtil;
import com.blog.framework.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Authenticator;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisUtil redisUtil;

    public String createUser(UserDTO  userDTO) throws IllegalArgumentException {
            Decoder decoder = Base64.getDecoder();
            String email = new String(decoder.decode(userDTO.getEmail()));
            String password = new String(decoder.decode(userDTO.getPassword()));
            userRepository.findByEmail(email).ifPresent(m -> {
                throw new IllegalArgumentException("이미 가입된 유저입니다.");
            });

            //비밀번호 암호화
            String encodepwd = passwordEncoder.encode( password);

            UserEntity savedUser = UserEntity.builder()
                    .phoneNumber(userDTO.getPhoneNumber())
                    .email(email)
                    .password(encodepwd)
                    .userName(userDTO.getUsername())
                    .build();

            //가입된 유저의 이름 반환
            return userRepository.save(savedUser).getUsername();
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserEntity) {
            return (UserEntity) authentication.getPrincipal();
        }
        return null; // 인증된 사용자가 없는 경우
    }

    public UserDTO login(LoginReqDTO reqDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(reqDTO.getEmail(), reqDTO.getPassword()));

        String token = jwtTokenUtil.createAccessToken(reqDTO);

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userInfo = userRepository.findByEmail(reqDTO.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        UserDTO resInfo = modelMapper.map(userInfo, UserDTO.class);
        resInfo.setToken(token);
        return resInfo;
    }

    public void logout(UserDTO user, HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long expirationTime = jwtTokenUtil.getExpiration(token);
        // Redis에 토큰 저장 (만료 시간 설정)
        redisUtil.setBlackList(token, true, expirationTime);

    }
}
