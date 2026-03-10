package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.SysOrder;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<SysOrder> {
    // 创建订单（结算核心）
    String createOrder(String username, String consignee, String phone, String address);

    // 查询用户订单列表
    List<SysOrder> getOrderList(String username);

    // 查询订单详情（含订单项）
    Map<String, Object> getOrderDetail(String orderNo);

    // 取消订单
    boolean cancelOrder(String orderNo, String username);

    // 更新订单为待发货状态
    boolean updateToSend(String orderNo);
}
