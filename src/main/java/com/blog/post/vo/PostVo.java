package com.blog.post.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostVo {
    private Long postId;
    private String title;
    private String content;
    private String description;
    private String imageUrl;
    private String authorNm;
}
