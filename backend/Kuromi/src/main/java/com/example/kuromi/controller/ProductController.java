package com.example.kuromi.controller;

import com.example.kuromi.common.Result;
import com.example.kuromi.entity.Product;
import com.example.kuromi.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller层：提供接口给前端调用
 */
@RestController // 标记为接口控制器，返回JSON数据
@RequestMapping("/product") // 接口统一前缀
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/test")
    public String test() {
        return "Hello, Product Controller!";
    }

    // 1. 按分类查询商品（对应前端 Mock 的 /api/products?category=hot）
    @GetMapping
    public Result<List<Product>> getProductList(
            @RequestParam(defaultValue = "hot") String category // 默认查hot分类
    ) {
        List<Product> list = productService.getProductListByCategory(category);
        return Result.success(list);
    }

    // 2. 搜索商品（对应前端 Mock 的 /api/products/search?keyword=xxx）
    @GetMapping("/search")
    public Result<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> list = productService.searchProducts(keyword);
        return Result.success(list);
    }

    // 3. 商品详情（对应前端 Mock 的 /api/products/detail?id=xxx&category=xxx）
    @GetMapping("/detail")
    public Result<Product> getProductDetail(
            @RequestParam String id,
            @RequestParam(defaultValue = "hot") String category
    ) {
        Product product = productService.getProductByIdAndCategory(id, category);
        if (product == null) {
            return Result.error( "商品不存在");
        }
        return Result.success(product);
    }

    @GetMapping("/all")
    public Result<List<Product>> getAllProducts() {
        List<Product> list = productService.getAllProducts();
        return Result.success(list);
    }
}