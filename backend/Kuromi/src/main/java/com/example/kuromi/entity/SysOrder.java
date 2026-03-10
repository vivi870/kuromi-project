package com.example.kuromi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sys_order")
public class SysOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;       // 订单编号
    private String username;      // 下单用户
    private BigDecimal totalAmount; // 总金额
    private Integer payStatus;    // 支付状态：0-待支付 1-待发货 2-已发货 3-已完成 4-已取消
    private String consignee;     // 收货人
    private String phone;         // 收货电话
    private String address;       // 收货地址

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
