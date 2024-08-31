package com.blog.framework.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @PrePersist
    protected void onCreate() {
        this.createdDttm = LocalDateTime.now();
        this.createId = getCurrentUserId();
        this.editedDttm = LocalDateTime.now();
        this.editId = getCurrentUserId();
    }

    @PreUpdate
    protected void onUpdate() {
        this.editedDttm = LocalDateTime.now();
        this.editId = getCurrentUserId();
    }


    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserEntity) {
            return ((UserEntity) principal).getId();
        }
        return null; // 또는 적절한 기본 값
    }
}
