package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.SysOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysOrderItemMapper extends BaseMapper<SysOrderItem> {
    // 根据订单号查询订单项
    List<SysOrderItem> selectByOrderNo(@Param("orderNo") String orderNo);
}
