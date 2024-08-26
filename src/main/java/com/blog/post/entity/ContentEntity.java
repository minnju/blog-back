package com.blog.post.entity;

import com.blog.framework.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CONTENT")
@EntityListeners(AuditingEntityListener.class)// 본인 테이블명과 맞춰주어야 함
public class ContentEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;
    private String content;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "postId")
    private PostEntity post;


}