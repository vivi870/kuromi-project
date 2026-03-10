package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车实体类，匹配前端cartItem结构
 */

@Data
@TableName("user_cart")
public class CartItem {
    /** 数据库自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（区分不同用户） */
    @TableField("username")
    private String username;

    /** 商品ID（对应前端cartItem的id） */
    @TableField("product_id")
    private String productId;

    /** 商品分类 */
    @TableField("category")
    private String category;

    /** 商品名称 */
    @TableField("name")
    private String name;

    /** 商品单价 */
    @TableField("price")
    private BigDecimal price;

    /** 商品图片完整URL */
    @TableField("image")
    private String image;

    /** 选中的尺寸 */
    @TableField("size")
    private String size;

    /** 选中的款式 */
    @TableField("style")
    private String style;

    /** 商品数量 */
    @TableField("quantity")
    private Integer quantity;

    /** 小计（单价*数量） */
    @TableField("subtotal")
    private BigDecimal subtotal;

    /** 是否选中（0=否，1=是） */
    @TableField("checked")
    private Integer checked;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
