package com.blog.post.entity;

import com.blog.framework.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POST")
@EntityListeners(AuditingEntityListener.class)// 본인 테이블명과 맞춰주어야 함
public class PostEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;
    private String description;
    private String imageUrl;
    private boolean isMain;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL)
    private ContentEntity content;


}