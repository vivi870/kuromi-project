package com.example.kuromi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.kuromi.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 更新用户头像
    @Update("UPDATE sys_user SET user_img = #{userImg}, update_time = NOW() WHERE id = #{userId}")
    int updateUserImg(Long userId, String userImg);

    // 更新用户名（username）
    @Update("UPDATE sys_user SET username = #{username}, update_username_time = NOW(), update_time = NOW() WHERE id = #{userId}")
    int updateNickname(Long userId, String username);
}
