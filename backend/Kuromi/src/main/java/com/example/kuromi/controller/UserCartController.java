package com.example.kuromi.controller;

import com.example.kuromi.common.Result;
import com.example.kuromi.entity.CartItem;
import com.example.kuromi.service.UserCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class UserCartController {

    @Autowired
    private UserCartService userCartService;

    // 获取当前用户购物车
    @GetMapping("/{username}")
    public Result<List<CartItem>> getCart(@PathVariable String username) {
        List<CartItem> cartList = userCartService.getCartByUsername(username);
        return Result.success(cartList);
    }

    // 添加商品到购物车
    @PostMapping("/add")
    public Result<Boolean> addToCart(@RequestBody CartItem cartItem) {
        boolean success = userCartService.addToCart(cartItem);
        return success ? Result.success(true) : Result.error("添加失败");
    }

    // 更新购物车商品
    @PutMapping("/update")
    public Result<Boolean> updateCartItem(@RequestBody CartItem cartItem) {
        boolean success = userCartService.updateCartItem(cartItem);
        return success ? Result.success(true) : Result.error("更新失败");
    }

    // 删除单个购物车商品
    @DeleteMapping("/remove")
    public Result<Boolean> removeCartItem(
            @RequestParam String username,
            @RequestParam Long productId,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String style
    ) {
        boolean success = userCartService.removeCartItemByParams(username, productId, size, style);
        return success ? Result.success(true) : Result.error("删除失败，商品不存在");
    }

    //批量删除购物车商品
    @PostMapping("/batch-remove")
    public Result<Boolean> batchRemoveCartItems(
            @RequestParam String username,
            @RequestBody List<CartItem> checked // 接收选中的商品列表
    ) {
        boolean success = userCartService.batchRemoveCartItems(username, checked);
        return success ? Result.success(true) : Result.error("批量删除失败");
    }

    // 清空购物车
    @DeleteMapping("/clear/{username}")
    public Result<Boolean> clearCart(@PathVariable String username) {
        boolean success = userCartService.clearCartByUsername(username);
        return success ? Result.success(true) : Result.error("清空失败");
    }
}
