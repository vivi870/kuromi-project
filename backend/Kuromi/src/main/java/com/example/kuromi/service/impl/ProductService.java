package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kuromi.entity.Product;
import com.example.kuromi.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service层：处理业务逻辑
 */
@Service // 标记为服务层组件
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    // 根据分类查询商品列表
    public List<Product> getProductListByCategory(String category) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("category", category)
                .eq("is_deleted", 0)
                .eq("status", 1); // 只显示上架商品
        return productMapper.selectList(wrapper);
    }

    // 搜索商品
    public List<Product> searchProducts(String keyword) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.like("name", keyword)
                .eq("is_deleted", 0)
                .eq("status", 1); // 只显示上架商品
        return productMapper.selectList(wrapper);
    }

    // 查询所有商品
    public List<Product> getAllProducts() {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0)
               .eq("status", 1); // 只显示上架商品
        return productMapper.selectList(wrapper);
    }

    // 按ID+分类查询商品详情
    public Product getProductByIdAndCategory(String id, String category) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id)
                .eq("category", category)
                .eq("is_deleted", 0)
                .eq("status", 1); // 只显示上架商品
        return productMapper.selectOne(wrapper);
    }
}