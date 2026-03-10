package com.example.kuromi.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email; // 对应前端注册表单的email字段
}
