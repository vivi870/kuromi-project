package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.UserAudit;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAuditMapper extends BaseMapper<UserAudit> {
}
