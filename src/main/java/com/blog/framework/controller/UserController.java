package com.blog.framework.controller;

import com.blog.framework.dto.LoginReqDTO;
import com.blog.framework.dto.UserDTO;
import com.blog.framework.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute LoginReqDTO reqDTO) {
        try {
            UserDTO userInfo = userService.login(reqDTO);
            return new  ResponseEntity<>(userInfo, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@ModelAttribute UserDTO user) throws IllegalArgumentException {
        String username = userService.createUser(user);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }


}
