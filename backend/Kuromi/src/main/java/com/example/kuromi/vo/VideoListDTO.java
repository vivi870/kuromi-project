package com.example.kuromi.vo;

import lombok.Data;

/**
 * 视频列表返回DTO（和前端Mock的字段完全对齐）
 */
@Data
public class VideoListDTO {
    private Long id;
    private String title;
    private String user;
    private String count;
    private String date;
    private String coverImg;
}
