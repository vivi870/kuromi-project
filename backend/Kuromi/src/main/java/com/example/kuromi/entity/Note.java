package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 笔记实体类
 * 对应数据库表：note
 */
@Data
@TableName("note")
public class Note {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 关联内容ID
     */
    private Long contentId;

    /**
     * 笔记类型：gl/tk/sp
     */
    private String type;

    /**
     * 笔记标题
     */
    private String title;

    /**
     * 笔记详情
     */
    private String detail;

    /**
     * 作者名称
     */
    private String author;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 是否点赞（0-未点赞，1-已点赞）
     */
    private Boolean liked;

    /**
     * 图片路径
     */
    @TableField(value = "imgUrl")
    private String imgUrl;

    /**
     * 用户头像路径
     */
    @TableField(value = "userimg")
    private String userImg;

    /**
     * 创建时间
     */
    @TableField(value = "createTime", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "updateTime", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false) // 标记为非数据库字段
    private List<?> comments;

    /**
     * 额外图片列表（逗号分隔，如 "gl1-1.jpg,gl1-2.jpg"）
     */
    private String imgs;
}
