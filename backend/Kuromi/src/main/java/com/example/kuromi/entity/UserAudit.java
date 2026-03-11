package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_audit")
public class UserAudit {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String username;
    /** 1=修改用户名 2=修改头像 */
    private Integer type;
    private String newValue;
    /** 0=待审核 1=通过 2=拒绝 */
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
