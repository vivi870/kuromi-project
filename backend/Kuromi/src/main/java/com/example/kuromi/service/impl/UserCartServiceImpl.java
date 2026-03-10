package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.entity.CartItem;
import com.example.kuromi.mapper.UserCartMapper;
import com.example.kuromi.service.UserCartService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserCartServiceImpl extends ServiceImpl<UserCartMapper, CartItem> implements UserCartService {

    @Override
    public List<CartItem> getCartByUsername(String username) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUsername, username);
        return this.list(wrapper);
    }

    @Override
    public boolean addToCart(CartItem cartItem) {
        // 先检查购物车中是否已有该商品（相同用户、product_id、size、style）
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUsername, cartItem.getUsername())
                .eq(CartItem::getProductId, cartItem.getProductId())
                .eq(CartItem::getSize, cartItem.getSize())
                .eq(CartItem::getStyle, cartItem.getStyle());

        CartItem existItem = this.getOne(wrapper);

        if (existItem != null) {
            // 如果商品已存在，更新数量和小计
            existItem.setQuantity(existItem.getQuantity() + cartItem.getQuantity());
            existItem.setSubtotal(existItem.getPrice().multiply(new BigDecimal(existItem.getQuantity())));
            return this.updateById(existItem);
        } else {
            // 如果商品不存在，新增一条记录
            cartItem.setSubtotal(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            return this.save(cartItem);
        }
    }

    @Override
    public boolean updateCartItem(CartItem cartItem) {
        // 1. 校验购物车项归属（防止越权修改）
        CartItem existingItem = this.getById(cartItem.getId());
        if (existingItem == null || !existingItem.getUsername().equals(cartItem.getUsername())) {
            throw new RuntimeException("无权修改该购物车项");
        }

        // 2. 保留原有小计计算逻辑
        cartItem.setSubtotal(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));

        // 3. 确保checked字段正确更新（兼容布尔/数字类型）
        if (cartItem.getChecked() == null) {
            cartItem.setChecked(0); // 默认未选中
        }

        // 4. 更新数据库
        return this.updateById(cartItem);
    }

    @Override
    public boolean removeCartItemByParams(String username, Long productId, String size, String style) {
        if (username == null || productId == null || productId <= 0) {
            return false;
        }
        // 构建多条件删除器
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("product_id", productId);
        // 非空才加条件（避免空值匹配不到）
        if (StringUtils.isNotBlank(size)) {
            wrapper.eq("size", size);
        }
        if (StringUtils.isNotBlank(style)) {
            wrapper.eq("style", style);
        }
        // 执行删除并返回受影响行数
        int affectedRows = this.baseMapper.delete(wrapper);
        return affectedRows > 0;
    }

    @Override
    public boolean batchRemoveCartItems(String username, List<CartItem> checked) {
        if (username == null || checked == null || checked.isEmpty()) {
            return false;
        }
        // 构建批量删除条件
        QueryWrapper<CartItem> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);

        // 拼接多组 (productId + size + style) 条件
        for (int i = 0; i < checked.size(); i++) {
            CartItem item = checked.get(i);
            if (item.getProductId() == null) continue;

            // 关键修复：直接在lambda中构建条件，而非传入已构建的QueryWrapper
            if (i == 0) {
                // 第一个条件：用and嵌套
                wrapper.and(w -> {
                    w.eq("product_id", item.getProductId());
                    if (StringUtils.isNotBlank(item.getSize())) {
                        w.eq("size", item.getSize());
                    }
                    if (StringUtils.isNotBlank(item.getStyle())) {
                        w.eq("style", item.getStyle());
                    }
                });
            } else {
                // 后续条件：用or嵌套
                wrapper.or(w -> {
                    w.eq("product_id", item.getProductId());
                    if (StringUtils.isNotBlank(item.getSize())) {
                        w.eq("size", item.getSize());
                    }
                    if (StringUtils.isNotBlank(item.getStyle())) {
                        w.eq("style", item.getStyle());
                    }
                });
            }
        }

        // 执行批量删除并返回结果
        int affectedRows = this.baseMapper.delete(wrapper);
        return affectedRows > 0;
    }

    @Override
    public boolean clearCartByUsername(String username) {
        LambdaQueryWrapper<CartItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CartItem::getUsername, username);
        return this.remove(wrapper);
    }
}
