package com.blog.framework.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AuditEntity {

    private Long createId;

    @CreatedDate
    private LocalDateTime createdDttm;

    private Long editId;

    @LastModifiedDate
    private LocalDateTime editedDttm;
}
