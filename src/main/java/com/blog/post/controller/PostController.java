package com.blog.post.controller;

import com.blog.framework.dto.LoginReqDTO;
import com.blog.framework.dto.UserDTO;
import com.blog.framework.service.UserService;
import com.blog.post.service.PostService;
import com.blog.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/save")
    public ResponseEntity<?> savePost(@ModelAttribute PostVo postVo) {
        boolean result = postService.savePost(postVo);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /*@DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@ModelAttribute UserDTO user) {
        String username = userService.createUser(user);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> postUser(@ModelAttribute UserDTO user) {
        String username = userService.createUser(user);
        return new ResponseEntity<>(username, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<UserDTO> login(@RequestBody @Validated LoginReqDTO user) {
        UserDTO userInfo = userService.login(user);
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }*/
}
