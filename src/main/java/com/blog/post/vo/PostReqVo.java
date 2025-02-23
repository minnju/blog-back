package com.blog.post.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostReqVo {
    private Long postId;
    private String title;
    private String content;
    private String description;
    private String imageUrl;
}
