package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.entity.CartItem;
import com.example.kuromi.entity.Product;
import com.example.kuromi.entity.SysOrder;
import com.example.kuromi.entity.SysOrderItem;
import com.example.kuromi.mapper.ProductMapper;
import com.example.kuromi.mapper.SysOrderItemMapper;
import com.example.kuromi.mapper.SysOrderMapper;
import com.example.kuromi.mapper.UserCartMapper;
import com.example.kuromi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<SysOrderMapper, SysOrder> implements OrderService {

    private final UserCartMapper userCartMapper;
    private final ProductMapper productMapper;
    private final SysOrderMapper sysOrderMapper;
    private final SysOrderItemMapper sysOrderItemMapper;

    // 创建订单（事务保证原子性）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(String username, String consignee, String phone, String address) {
        // 1. 查询用户选中的购物车商品
        List<CartItem> checkedItems = userCartMapper.selectCheckedItems(username);
        if (checkedItems.isEmpty()) {
            throw new RuntimeException("请选择要结算的商品");
        }

        // 2. 校验库存并计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : checkedItems) {
            Product product = productMapper.selectById(cartItem.getProductId());
            if (product == null || product.getIsDeleted() == 1) {
                throw new RuntimeException("商品[" + cartItem.getName() + "]已下架");
            }
            // 库存校验
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("商品[" + cartItem.getName() + "]库存不足");
            }
            // 累加总金额
            totalAmount = totalAmount.add(cartItem.getSubtotal());
        }

        // 3. 生成订单编号（时间戳+随机数+用户ID后4位）
        String orderNo = generateOrderNo(username);

        // 4. 保存订单主表
        SysOrder order = new SysOrder();
        order.setOrderNo(orderNo);
        order.setUsername(username);
        order.setTotalAmount(totalAmount);
        order.setPayStatus(0); // 0-待支付
        order.setConsignee(consignee);
        order.setPhone(phone);
        order.setAddress(address);
        sysOrderMapper.insert(order);

        // 5. 保存订单项
        for (CartItem cartItem : checkedItems) {
            SysOrderItem orderItem = new SysOrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setOrderNo(orderNo);
            orderItem.setUsername(username);
            orderItem.setProductId(Long.valueOf(cartItem.getProductId()));
            orderItem.setProductName(cartItem.getName());
            orderItem.setProductImg(cartItem.getImage());
            orderItem.setCategory(cartItem.getCategory());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItem.setSize(cartItem.getSize());
            orderItem.setStyle(cartItem.getStyle());
            sysOrderItemMapper.insert(orderItem);

            // 6. 扣减商品库存
            Product product = productMapper.selectById(cartItem.getProductId());
            product.setStock(product.getStock() - cartItem.getQuantity());
            productMapper.updateById(product);
        }

        // 7. 删除购物车中已结算的商品
        userCartMapper.deleteCheckedItems(username);

        return orderNo;
    }

    // 生成订单编号
    private String generateOrderNo(String username) {
        // 时间戳（yyyyMMddHHmmss） + 随机数（4位） + 用户ID后4位（这里简化为用户名后4位）
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.valueOf(new Random().nextInt(9000) + 1000);
        String suffix = username.length() >= 4 ? username.substring(username.length() - 4) : username;
        return time + random + suffix;
    }

    // 查询用户订单列表
    @Override
    public List<SysOrder> getOrderList(String username) {
        return sysOrderMapper.selectByUsername(username);
    }

    // 查询订单详情
    @Override
    public Map<String, Object> getOrderDetail(String orderNo) {
        Map<String, Object> result = new HashMap<>();
        // 查询订单主信息
        SysOrder order = lambdaQuery().eq(SysOrder::getOrderNo, orderNo).one();
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 查询订单项
        List<SysOrderItem> items = sysOrderItemMapper.selectByOrderNo(orderNo);
        result.put("order", order);
        result.put("items", items);
        return result;
    }

    // 取消订单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelOrder(String orderNo, String username) {
        // 1. 查询订单
        SysOrder order = lambdaQuery()
                .eq(SysOrder::getOrderNo, orderNo)
                .eq(SysOrder::getUsername, username)
                .one();
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 3. 更新订单状态为已取消
        order.setPayStatus(4);
        boolean updateResult = updateById(order);

        // 4. 恢复商品库存
        List<SysOrderItem> items = sysOrderItemMapper.selectByOrderNo(orderNo);
        for (SysOrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            product.setStock(product.getStock() + item.getQuantity());
            productMapper.updateById(product);
        }
        return updateResult;
    }

    // 更新订单为待发货
    @Override
    public boolean updateToSend(String orderNo) {
        SysOrder order = lambdaQuery().eq(SysOrder::getOrderNo, orderNo).one();
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        // 只有待支付（已支付）的订单可改为待发货
        if (order.getPayStatus() != 0) { // 实际项目中这里应该是已支付状态，简化为0
            throw new RuntimeException("仅待支付订单可改为待发货");
        }
        order.setPayStatus(1);
        return updateById(order);
    }
}
