package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类，和数据库product表一一对应
 */
@Data // Lombok注解，自动生成get/set方法（需引入Lombok依赖，见下文）
@TableName("product") // 指定对应数据库表名
public class Product {
    // 主键ID，对应数据库id字段
    @TableId(type = IdType.AUTO)
    private Long id;
    // 商品名称
    private String name;
    // 商品分类（hot/mh/wo/zb/qt）
    private String category;
    // 商品价格（用BigDecimal避免浮点精度问题）
    private BigDecimal price;
    // 销量
    private Integer sales;
    // 商品主图URL
    private String imgUrl;
    // 角标图片URL（仅hot分类）
    private String cornerImgUrl;
    // 款式（JSON格式，数据库是JSON类型，这里用String接收）
    private String styles;
    // 商品多图（JSON数组）
    private String imgs;
    // 详情图（JSON数组）
    private String detailImgs;
    // 尺寸（JSON格式）
    private String sizes;
    // 逻辑删除标识
    private Integer isDeleted;
    //库存
    private Integer stock;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}