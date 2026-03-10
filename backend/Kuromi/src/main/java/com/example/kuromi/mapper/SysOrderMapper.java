package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.SysOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysOrderMapper extends BaseMapper<SysOrder> {
    // 根据用户名查询订单列表
    List<SysOrder> selectByUsername(@Param("username") String username);
}
