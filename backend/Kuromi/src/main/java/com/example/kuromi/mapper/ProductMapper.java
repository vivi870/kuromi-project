package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * Mapper层：直接操作数据库，继承BaseMapper获得CRUD方法
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // MyBatis-Plus自动提供selectList、selectById等方法，无需手写SQL
}