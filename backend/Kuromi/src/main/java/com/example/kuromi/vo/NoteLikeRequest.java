package com.example.kuromi.vo;

import lombok.Data;

/**
 * 点赞请求参数
 */
@Data
public class NoteLikeRequest {
    private Long noteId;
    private Boolean isLiked;
    private String type;
}
