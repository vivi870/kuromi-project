package com.example.kuromi.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 笔记详情返回VO
 */
@Data
public class NoteDetailResponse {
    private Integer code;
    private String message;
    private NoteDetailData data;

    /**
     * 笔记详情数据体
     */
    @Data
    public static class NoteDetailData {
        private Long id;
        private String title;
        private String contentId;
        private String detail; // 笔记详情字段
        private String type;
        private String author;
        private Integer likes;
        private Boolean liked;
        private String imgUrl;
        private String userimg;
        private LocalDateTime createTime; // 发布时间
        private LocalDateTime updateTime;
    }
}
