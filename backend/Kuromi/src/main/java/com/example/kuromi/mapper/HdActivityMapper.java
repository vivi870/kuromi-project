package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.kuromi.entity.hd;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HdActivityMapper extends BaseMapper<hd> {
    @Select("SELECT id, title, time_range AS time, img_url AS imgUrl FROM hd")
    List<hd> selectAllHdActivity();

    @Select("SELECT id, title, time_range AS time, img_url AS imgUrl FROM hd WHERE title LIKE #{keyword}")
    List<hd> selectHdByKeyword(@Param("keyword") String keyword);
}
