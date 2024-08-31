package com.blog.post.controller;

import com.blog.framework.dto.LoginReqDTO;
import com.blog.framework.dto.UserDTO;
import com.blog.framework.service.UserService;
import com.blog.post.service.PostService;
import com.blog.post.vo.PostReqVo;
import com.blog.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @PostMapping("/save")
    public ResponseEntity<?> savePost(@ModelAttribute PostVo postVo) {
        boolean result = postService.savePost(postVo);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam(value="postId") long postId) throws Exception {
        boolean isDeleted = postService.deletePost(postId);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> postUser(@ModelAttribute PostVo postVo) {
        boolean isUpdated = postService.updatePost(postVo);
        return new ResponseEntity<>(isUpdated, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PostVo>> login(@RequestBody PostReqVo reqVo) {
        List<PostVo> postList = postService.retrieveAllPost(reqVo);
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }
}
