package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.entity.SysUser;
import com.example.kuromi.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    private final BCryptPasswordEncoder passwordEncoder;

    // 注入加密器
    public SysUserService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // 检查用户名是否存在
    public boolean checkUsernameExists(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.count(queryWrapper) > 0;
    }

    // 登录验证
    public SysUser login(String username, String password) {
        // 1. 先根据用户名查询用户（查不到直接返回null）
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser user = this.getOne(queryWrapper);

        if (user == null) {
            System.out.println("登录失败：用户名不存在 - " + username); // 打印日志，方便排查
            return null;
        }

        // 2. 验证密码（BCrypt匹配）
        boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
        if (!passwordMatch) {
            System.out.println("登录失败：密码不匹配 - 用户名：" + username); // 打印日志
            return null;
        }

        // 3. 验证通过，返回用户
        return user;
    }

    // 配置头像存储路径（建议在application.yml中配置）
    @Value("${user.avatar.path:D:/project/avatar/}") // 本地路径，可替换为服务器路径
    private String avatarPath;

    // 上传头像
    public String uploadUserImg(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传文件不能为空");
        }
        // 1. 创建存储目录（不存在则创建）
        File dir = new File(avatarPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 2. 生成唯一文件名（避免重复）
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + suffix;
        // 3. 保存文件
        try {
            file.transferTo(new File(avatarPath + newFileName));
        } catch (IOException e) {
            throw new RuntimeException("头像上传失败：" + e.getMessage());
        }
        // 4. 更新数据库（存储相对路径/文件名）
        baseMapper.updateUserImg(userId, newFileName);
        // 5. 返回头像访问路径（context-path=/api，资源映射=/avatar/**，所以返回 /avatar/）
        return "/avatar/" + newFileName;
    }

    // 修改用户名（昵称）
    public boolean updateNickname(Long userId, String newNickname) {
        if (newNickname == null || newNickname.trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        int rows = baseMapper.updateNickname(userId, newNickname);
        return rows > 0;
    }
}