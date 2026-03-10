package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("hd") // 对应数据库表名
public class hd{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    @TableField(value = "time_range")
    private String time;
    @TableField(value = "img_url")
    private String imgUrl;
    private String imgUrls;
}
