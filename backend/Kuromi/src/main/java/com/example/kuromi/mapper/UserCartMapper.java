package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserCartMapper extends BaseMapper<CartItem> {
    // 查询用户选中的购物车商品
    List<CartItem> selectCheckedItems(@Param("username") String username);

    // 删除用户选中的购物车商品
    int deleteCheckedItems(@Param("username") String username);
}
