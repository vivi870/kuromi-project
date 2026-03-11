package com.example.kuromi.controller;

import com.example.kuromi.common.Result;
import com.example.kuromi.dto.LoginRequest;
import com.example.kuromi.dto.LoginResponse;
import com.example.kuromi.dto.RegisterRequest;
import com.example.kuromi.entity.SysUser;
import com.example.kuromi.entity.UserAudit;
import com.example.kuromi.mapper.UserAuditMapper;
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
    private final UserAuditMapper userAuditMapper;

    public UserController(SysUserService sysUserService,
                          BCryptPasswordEncoder passwordEncoder,
                          UserAuditMapper userAuditMapper) {
        this.sysUserService = sysUserService;
        this.passwordEncoder = passwordEncoder;
        this.userAuditMapper = userAuditMapper;
    }

    /** 登录 */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        SysUser user = sysUserService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        // 封禁检查
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error("该账号已被封禁，请联系管理员");
        }
        HttpSession session = request.getSession(true);
        session.setAttribute("loginUser", user);
        session.setMaxInactiveInterval(1800);
        LoginResponse response = new LoginResponse();
        response.setUsername(user.getUsername());
        response.setIsLoggedIn(true);
        response.setNickname(user.getNickname());
        response.setUserImg(user.getUserImg());
        return Result.success(response);
    }

    /** 注册 */
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest registerRequest) {
        try {
            if (sysUserService.checkUsernameExists(registerRequest.getUsername())) {
                return Result.error("用户名已存在");
            }
            String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
            SysUser newUser = new SysUser();
            newUser.setUsername(registerRequest.getUsername());
            newUser.setPassword(encodedPassword);
            newUser.setNickname("新用户");
            newUser.setEmail(registerRequest.getEmail());
            newUser.setStatus(1);
            sysUserService.save(newUser);
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /** 退出登录 */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("loginUser");
            session.invalidate();
        }
        return Result.success();
    }

    /**
     * 上传头像 - 改为提交审核，审核通过后才更新
     */
    @PostMapping("/upload/avatar")
    public ResponseEntity<Map<String, Object>> uploadAvatar(
            @RequestParam("file") MultipartFile file,
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
            // 封禁检查
            if (loginUser.getStatus() != null && loginUser.getStatus() == 0) {
                result.put("code", 403);
                result.put("msg", "账号已被封禁");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            }
            // 先上传文件到临时路径（复用原有上传逻辑）
            String avatarUrl = sysUserService.uploadUserImg(loginUser.getId(), file);
            // 上传成功后，回滚数据库（不直接更新，改为提交审核）
            // 恢复原头像
            loginUser.setUserImg(loginUser.getUserImg()); // 不改session
            // 把原有 updateUserImg 改回去（审核通过才更新）
            sysUserService.getById(loginUser.getId()); // 重新查一次保证最新
            // 撤销刚才的直接更新：恢复原userImg
            com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<SysUser> uw =
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
            uw.eq(SysUser::getId, loginUser.getId()).set(SysUser::getUserImg, loginUser.getUserImg());
            sysUserService.update(uw);
            // 提交审核记录
            UserAudit audit = new UserAudit();
            audit.setUserId(loginUser.getId());
            audit.setUsername(loginUser.getUsername());
            audit.setType(2); // 2=头像
            audit.setNewValue(avatarUrl);
            audit.setStatus(0); // 待审核
            userAuditMapper.insert(audit);
            result.put("code", 200);
            result.put("msg", "头像已提交审核，审核通过后生效");
            result.put("data", avatarUrl);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "上传失败：" + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 修改用户名 - 改为提交审核
     */
    @PostMapping("/update/nickname")
    public ResponseEntity<Map<String, Object>> updateNickname(
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
            // 封禁检查
            if (loginUser.getStatus() != null && loginUser.getStatus() == 0) {
                result.put("code", 403);
                result.put("msg", "账号已被封禁");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
            }
            String newNickname = params.get("newNickname");
            if (newNickname == null || newNickname.trim().isEmpty()) {
                result.put("code", 400);
                result.put("msg", "用户名不能为空");
                return ResponseEntity.badRequest().body(result);
            }
            // 提交审核记录，不直接更新
            UserAudit audit = new UserAudit();
            audit.setUserId(loginUser.getId());
            audit.setUsername(loginUser.getUsername());
            audit.setType(1); // 1=用户名
            audit.setNewValue(newNickname.trim());
            audit.setStatus(0); // 待审核
            userAuditMapper.insert(audit);
            result.put("code", 200);
            result.put("msg", "用户名修改已提交审核，审核通过后生效");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "修改失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /** 获取登录用户信息 */
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
        // 刷新最新状态（防止封禁后session未更新）
        SysUser freshUser = sysUserService.getById(loginUser.getId());
        if (freshUser != null) {
            session.setAttribute("loginUser", freshUser);
            loginUser = freshUser;
        }
        result.put("code", 200);
        result.put("data", loginUser);
        return ResponseEntity.ok(result);
    }
}
