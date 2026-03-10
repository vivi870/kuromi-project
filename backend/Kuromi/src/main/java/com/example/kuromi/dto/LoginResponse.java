package com.example.kuromi.dto;

import lombok.Data;

/**
 * 登录响应数据
 */
@Data
public class LoginResponse {
    /** 用户名 */
    private String username;
    /** 是否登录成功 */
    private Boolean isLoggedIn;
    /** 昵称（可选） */
    private String nickname;
}