package com.blog.framework.controller;

import com.blog.framework.dto.LoginReqDTO;
import com.blog.framework.dto.UserDTO;
import com.blog.framework.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@ModelAttribute UserDTO user) {
        String username = userService.createUser(user);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PostMapping("/login_v1")
    public ResponseEntity<UserDTO> login(@RequestBody @Validated LoginReqDTO user) {
        UserDTO userInfo = userService.login(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}
