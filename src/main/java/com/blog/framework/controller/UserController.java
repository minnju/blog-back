package com.blog.framework.controller;

import com.blog.framework.dto.UserDTO;
import com.blog.framework.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> postUser(@RequestBody UserDTO user) {
        String username = userService.createUser(user);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }
}
