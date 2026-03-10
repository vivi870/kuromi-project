package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.SysUser;
import org.springframework.web.multipart.MultipartFile;

public interface SysUserService extends IService<SysUser> {
    // 上传头像
    String uploadUserImg(Long userId, MultipartFile file);

    // 修改用户名（昵称）
    boolean updateNickname(Long userId, String newNickname);
}
