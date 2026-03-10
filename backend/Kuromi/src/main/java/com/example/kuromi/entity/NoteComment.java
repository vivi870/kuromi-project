package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("note_comment")
public class NoteComment {
    /** 评论ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联笔记ID */
    private Long noteId;

    /** 关联笔记类型（gl/tk/sp） */
    private String noteType;

    /** 评论用户ID */
    private Long userId;

    /** 评论用户名（冗余） */
    private String username;

    /** 评论内容 */
    private String content;

    /** 评论时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 逻辑删除 0-未删 1-已删 */
    @TableLogic
    private Integer isDeleted;
}
