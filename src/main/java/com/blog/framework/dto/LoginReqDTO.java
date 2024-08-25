package com.blog.framework.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Collection;

@Getter
public class LoginReqDTO{
    @NotBlank(message = "email은 필수입니다.")
    private String email;
    @NotBlank(message = "password는 필수입니다.")
    private String password;
}

