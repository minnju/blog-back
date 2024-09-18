package com.blog.post.entity;

import com.blog.framework.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POST")
@EntityListeners(AuditingEntityListener.class)
public class PostEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;
    private String description;
    private String content;
    private String imageUrl;
    private boolean isMain;
    private String authorNm;

    @OneToMany(mappedBy="post" ,fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    private List<CommentEntity> comments  = new ArrayList<>();

    public void addComment(CommentEntity comment){
        this.getComments().add(comment);
        comment.setPost(this);
    }




}