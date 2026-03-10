package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("video") // 对应数据库表名
public class Video {
    @TableId(type = IdType.AUTO)
    private Long id;          // 视频ID
    private String title;     // 视频标题
    private String user;      // 发布用户
    private String count;     // 播放量（如21万、71.5万）
    private String date;      // 发布日期
    @TableField("cover_img")
    private String coverImg;  // 封面图片名称（如spfm1.jpg）
    @TableField("video_file")
    private String videoFile; // 视频文件名称（如sp1.mp4）
    @TableField("is_deleted")
    private Integer isDeleted = 0; // 删除标记
    @TableField("create_time")
    private LocalDateTime createTime; // 创建时间
}
