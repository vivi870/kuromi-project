package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_order_item")
public class SysOrderItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long orderId;         // 订单ID
    private String orderNo;       // 订单编号
    private String username;      //下单用户
    private Long productId;       // 商品ID
    private String productName;   // 商品名称
    private String productImg;    // 商品图片
    private String category;      // 商品分类
    private BigDecimal price;     // 商品单价
    private Integer quantity;     // 购买数量
    private BigDecimal subtotal;  // 小计金额
    private String size;          // 商品尺寸
    private String style;         // 商品款式

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
