package com.example.kuromi.controller;

import com.example.kuromi.common.Result;
import com.example.kuromi.dto.LoginRequest;
import com.example.kuromi.dto.LoginResponse;
import com.example.kuromi.dto.RegisterRequest;
import com.example.kuromi.entity.SysUser;
import com.example.kuromi.service.impl.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SysUserService sysUserService;
    private final BCryptPasswordEncoder passwordEncoder;

    // 构造器注入
    public UserController(SysUserService sysUserService, BCryptPasswordEncoder passwordEncoder) {
        this.sysUserService = sysUserService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 登录接口（核心修复：登录成功后存入Session）
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        // 1. 调用Service验证登录
        SysUser user = sysUserService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 2. 验证失败
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 3. 🌟 核心修复：登录成功后，将用户信息存入Session
        HttpSession session = request.getSession(true); // true：不存在则创建Session
        session.setAttribute("loginUser", user); // 存入用户信息，键名必须是"loginUser"
        session.setMaxInactiveInterval(1800); // Session有效期30分钟（可选）

        // 4. 构造响应数据（匹配前端Pinia的字段）
        LoginResponse response = new LoginResponse();
        response.setUsername(user.getUsername());
        response.setIsLoggedIn(true);
        response.setNickname(user.getNickname());
        response.setUserImg(user.getUserImg());

        return Result.success(response);
    }

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            // 1. 检查用户名是否已存在
            if (sysUserService.checkUsernameExists(registerRequest.getUsername())) {
                return Result.error("用户名已存在");
            }

            // 2. 加密密码（复用注入的加密器，避免重复new）
            String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());

            // 3. 保存用户到数据库
            SysUser newUser = new SysUser();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(encodedPassword);
            newUser.setNickname("新用户"); // 默认昵称
            newUser.setEmail(registerRequest.getEmail());
            sysUserService.save(newUser);

            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 退出登录接口（核心修复：清空Session）
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // 🌟 清空Session中的用户信息
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("loginUser");
            session.invalidate(); // 销毁Session
        }
        return Result.success();
    }

    /**
     * 上传用户头像（增加调试日志，方便定位问题）
     */
    @PostMapping("/upload/avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调试：打印Session信息
            HttpSession session = request.getSession(false);
            System.out.println("=== 头像上传调试 ===");
            System.out.println("Session是否存在：" + (session != null));
            if (session != null) {
                System.out.println("Session ID：" + session.getId());
                SysUser loginUser = (SysUser) session.getAttribute("loginUser");
                System.out.println("loginUser是否存在：" + (loginUser != null));
                if (loginUser != null) {
                    System.out.println("登录用户ID：" + loginUser.getId());
                }
            }

            // 1. 校验登录状态
            if (session == null || session.getAttribute("loginUser") == null) {
                result.put("code", 401);
                result.put("msg", "未登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            // 2. 获取登录用户
            SysUser loginUser = (SysUser) session.getAttribute("loginUser");

            // 3. 调用Service上传并更新
            String avatarUrl = sysUserService.uploadUserImg(loginUser.getId(), file);

            // 4. 更新Session中的头像信息
            loginUser.setUserImg(avatarUrl);
            session.setAttribute("loginUser", loginUser);

            result.put("code", 200);
            result.put("msg", "上传成功");
            result.put("data", avatarUrl);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "上传失败：" + e.getMessage());
            e.printStackTrace(); // 打印异常栈，方便调试
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 修改用户名（昵称）
     */
    @PostMapping("/update/nickname")
    public ResponseEntity<Map<String, Object>> updateNickname(
            // 🌟 关键：用 Map 接收 JSON 数据
            @RequestBody Map<String, String> params,
            HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                result.put("code", 401);
                result.put("msg", "未登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }
            SysUser loginUser = (SysUser) session.getAttribute("loginUser");

            // 从 params 中取出 newNickname
            String newNickname = params.get("newNickname");
            boolean success = sysUserService.updateNickname(loginUser.getId(), newNickname);
            if (success) {
                // 同步更新 Session 中的 username 和 nickname
                loginUser.setUsername(newNickname);
                loginUser.setNickname(newNickname);
                session.setAttribute("loginUser", loginUser);
                result.put("code", 200);
                result.put("msg", "用户名修改成功");
            } else {
                result.put("code", 400);
                result.put("msg", "用户名修改失败");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "修改失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 获取登录用户信息（含头像）
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            result.put("code", 401);
            result.put("msg", "未登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        SysUser loginUser = (SysUser) session.getAttribute("loginUser");
        result.put("code", 200);
        result.put("data", loginUser);
        return ResponseEntity.ok(result);
    }
}