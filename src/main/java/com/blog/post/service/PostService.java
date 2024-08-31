package com.blog.post.service;

import com.blog.framework.entity.UserEntity;
import com.blog.framework.service.UserService;
import com.blog.post.entity.ContentEntity;
import com.blog.post.entity.PostEntity;
import com.blog.post.repository.PostRespository;
import com.blog.post.vo.PostReqVo;
import com.blog.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRespository postRespository;
    private final UserService  userService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean savePost(PostVo postVo) {
        UserEntity user = userService.getCurrentUser();

        PostEntity post = PostEntity.builder()
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .description(postVo.getDescription())
                .imageUrl(postVo.getImageUrl())
                .authorNm(user.getUsername())
                .build();

        postRespository.saveAndFlush(post);

        return true;
    }

    @Transactional(readOnly = true)
    public List<PostVo> retrieveAllPost(PostReqVo reqVo){
        List<PostEntity> postList = postRespository.findAll();
        return postList.stream()
                .map(postEntity -> new PostVo(postEntity.getPostId(), postEntity.getTitle(), postEntity.getContent(),postEntity.getDescription(), postEntity.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deletePost(long postId) throws Exception {
        PostEntity deleteEntity = postRespository.findByPostId(postId).orElseThrow(()->new Exception("존재하지 않는 게시물입니다."));
        postRespository.delete(deleteEntity);
        postRespository.flush();
        return true;
    }

    public boolean updatePost(PostVo postVo) {
        return true;
    }
}
