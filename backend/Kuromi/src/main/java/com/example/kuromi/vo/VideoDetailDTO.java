package com.example.kuromi.vo;

import lombok.Data;

/**
 * 单个视频详情返回DTO（和前端Mock的字段完全对齐）
 */
@Data
public class VideoDetailDTO {
    private Long id;
    private String title;
    private String user;
    private String count;
    private String date;
    private String coverImg;
    private String videoUrl; // 对应数据库的videoFile字段
}
