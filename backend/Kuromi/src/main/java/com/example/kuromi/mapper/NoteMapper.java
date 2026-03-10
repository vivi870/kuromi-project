package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 笔记数据访问层
 */
@Mapper
public interface NoteMapper extends BaseMapper<Note> {

    /**
     * 根据类型查询笔记列表
     * @param type 笔记类型（gl/tk/sp）
     * @return 对应类型的笔记列表
     */
    @Select("SELECT * FROM note WHERE type = #{type} ORDER BY id DESC")
    List<Note> selectByType(@Param("type") String type);
}
