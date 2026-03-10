package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.CartItem;

import java.util.List;

public interface UserCartService extends IService<CartItem> {
    // 获取当前用户的购物车列表
    List<CartItem> getCartByUsername(String username);

    // 添加商品到购物车
    boolean addToCart(CartItem cartItem);

    // 更新购物车商品（数量、选中状态等）
    boolean updateCartItem(CartItem cartItem);

    // 删除单个购物车商品
    boolean removeCartItemByParams(String username, Long productId, String size, String style);

    //批量删除选中的商品
    boolean batchRemoveCartItems(String username, List<CartItem> checked);

    // 清空当前用户的购物车
    boolean clearCartByUsername(String username);
}
