package com.blog.post.entity;

import com.blog.framework.entity.AuditEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="LIKE_TABLE")
@EntityListeners(AuditingEntityListener.class)
public class LikeEntity extends AuditEntity {
    @EmbeddedId
    private LikeId likeId;

    private boolean likeYn;

    @MapsId("postId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "postId")
    private PostEntity post;




}