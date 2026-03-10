package com.example.kuromi.vo;

import lombok.Data;

/**
 * 点赞返回结果
 */
@Data
public class NoteLikeResponse {
    private Integer code;
    private String message;
    private NoteLikeData data;

    @Data
    public static class NoteLikeData {
        private String id;
        private String type;
        private Boolean liked;
        private Integer likes;
    }
}
