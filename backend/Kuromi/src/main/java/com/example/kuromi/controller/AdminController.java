package com.example.kuromi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kuromi.common.Result;
import com.example.kuromi.entity.*;
import com.example.kuromi.mapper.AdminMapper;
import com.example.kuromi.mapper.ProductMapper;
import com.example.kuromi.mapper.HdActivityMapper;
import com.example.kuromi.service.VideoService;
import com.example.kuromi.entity.Video;
import com.example.kuromi.entity.hd;
import com.example.kuromi.mapper.UserAuditMapper;
import com.example.kuromi.service.NoteCommentService;
import com.example.kuromi.service.NoteService;
import com.example.kuromi.service.OrderService;
import com.example.kuromi.service.impl.SysUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminMapper adminMapper;
    private final SysUserService sysUserService;
    private final NoteService noteService;
    private final NoteCommentService noteCommentService;
    private final OrderService orderService;
    private final PasswordEncoder passwordEncoder;
    private final UserAuditMapper userAuditMapper;
    private final ProductMapper productMapper;
    private final HdActivityMapper hdActivityMapper;
    private final VideoService videoService;

    private Admin getLoginAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (Admin) session.getAttribute("loginAdmin");
    }
    private boolean notAdmin(HttpServletRequest request) { return getLoginAdmin(request) == null; }

    @GetMapping("/gen-password")
    public Result genPassword(@RequestParam String raw) { return Result.success(passwordEncoder.encode(raw)); }    // login
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params, HttpServletRequest request) {
        String username = params.get("username"); String password = params.get("password");
        if (username == null || password == null) return Result.error("账号和密码不能为空");
        Admin admin = adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getUsername, username));
        if (admin == null) return Result.error("账号不存在");
        if (admin.getStatus() == 0) return Result.error("账号已被禁用");
        if (!passwordEncoder.matches(password, admin.getPassword())) return Result.error("密码错误");
        HttpSession session = request.getSession(true); admin.setPassword(null);
        session.setAttribute("loginAdmin", admin);
        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId()); data.put("username", admin.getUsername());
        data.put("nickname", admin.getNickname()); data.put("role", admin.getRole());
        return Result.success(data);
    }
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return Result.success("已退出");
    }
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        Admin admin = getLoginAdmin(request); if (admin == null) return Result.error("未登录");
        Map<String, Object> data = new HashMap<>();
        data.put("id", admin.getId()); data.put("username", admin.getUsername());
        data.put("nickname", admin.getNickname()); data.put("role", admin.getRole());
        return Result.success(data);
    }
    @GetMapping("/dashboard")
    public Result dashboard(HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", sysUserService.count());
        data.put("noteCount", noteService.count());
        data.put("commentCount", noteCommentService.count());
        data.put("orderCount", orderService.count());
        data.put("productCount", productMapper.selectCount(new LambdaQueryWrapper<Product>().eq(Product::getIsDeleted, 0)));
        return Result.success(data);
    }    // 商品管理
    @GetMapping("/products")
    public Result getProducts(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String keyword, @RequestParam(required=false) String category,
            @RequestParam(required=false) Integer status, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        LambdaQueryWrapper<Product> w = new LambdaQueryWrapper<>();
        w.eq(Product::getIsDeleted, 0);
        if (keyword != null && !keyword.isBlank()) w.like(Product::getName, keyword);
        if (category != null && !category.isBlank()) w.eq(Product::getCategory, category);
        if (status != null) w.eq(Product::getStatus, status);
        w.orderByDesc(Product::getId);
        IPage<Product> r = productMapper.selectPage(new Page<>(page, size), w);
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }
    @PostMapping("/products/{id}/status")
    public Result updateProductStatus(@PathVariable Long id, @RequestBody Map<String,Integer> body, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        Integer status = body.get("status"); if (status == null) return Result.error("状态不能为空");
        productMapper.update(null, new LambdaUpdateWrapper<Product>().eq(Product::getId,id).set(Product::getStatus,status));
        return Result.success(status==1?"已上架":"已下架");
    }

    @PostMapping("/products/upload-img")
    public Result uploadProductImg(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        if (file == null || file.isEmpty()) return Result.error("文件不能为空");
        try {
            java.io.File dir = new java.io.File("D:/project/spimg/");
            if (!dir.exists()) dir.mkdirs();
            String original = file.getOriginalFilename();
            String suffix = original != null && original.contains(".") ? original.substring(original.lastIndexOf(".")) : ".jpg";
            String fileName = java.util.UUID.randomUUID() + suffix;
            file.transferTo(new java.io.File("D:/project/spimg/" + fileName));
            // 返回文件名，前端用 /prodimg/ 前缀访问
            return Result.success(fileName);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/products/add")
    public Result addProduct(@RequestBody Product product, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        product.setIsDeleted(0);
        if (product.getStatus() == null) product.setStatus(1);
        if (product.getSales() == null) product.setSales(0);
        productMapper.insert(product);
        return Result.success("商品添加成功");
    }
    // 审核管理
    @GetMapping("/audits")
    public Result getAudits(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) Integer status, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        LambdaQueryWrapper<UserAudit> w = new LambdaQueryWrapper<>();
        if (status != null) w.eq(UserAudit::getStatus, status);
        w.orderByDesc(UserAudit::getCreateTime);
        IPage<UserAudit> r = userAuditMapper.selectPage(new Page<>(page,size), w);
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }
    @PostMapping("/audits/{id}/approve")
    public Result approveAudit(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        UserAudit audit = userAuditMapper.selectById(id);
        if (audit==null) return Result.error("不存在"); if (audit.getStatus()!=0) return Result.error("已处理");
        SysUser user = sysUserService.getById(audit.getUserId()); if (user==null) return Result.error("用户不存在");
        if (audit.getType()==1){user.setUsername(audit.getNewValue());user.setNickname(audit.getNewValue());}
        else if(audit.getType()==2) user.setUserImg(audit.getNewValue());
        sysUserService.updateById(user); audit.setStatus(1); userAuditMapper.updateById(audit);
        return Result.success("已通过");
    }
    @PostMapping("/audits/{id}/reject")
    public Result rejectAudit(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        UserAudit audit = userAuditMapper.selectById(id);
        if (audit==null) return Result.error("不存在"); if (audit.getStatus()!=0) return Result.error("已处理");
        audit.setStatus(2); userAuditMapper.updateById(audit);
        return Result.success("已拒绝");
    }    // 用户管理
    @GetMapping("/users")
    public Result getUsers(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String keyword, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        LambdaQueryWrapper<SysUser> w = new LambdaQueryWrapper<>();
        if (keyword!=null && !keyword.isBlank()) w.like(SysUser::getUsername,keyword).or().like(SysUser::getNickname,keyword);
        w.orderByDesc(SysUser::getCreateTime);
        IPage<SysUser> r = sysUserService.page(new Page<>(page,size), w);
        r.getRecords().forEach(u -> u.setPassword(null));
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }
    @PostMapping("/users/{id}/status")
    public Result updateUserStatus(@PathVariable Long id, @RequestBody Map<String,Integer> body, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        SysUser user = sysUserService.getById(id); if (user==null) return Result.error("用户不存在");
        Integer status = body.get("status"); if (status==null) return Result.error("状态不能为空");
        user.setStatus(status); sysUserService.updateById(user);
        return Result.success(status==1?"已启用":"已封禁");
    }
    @DeleteMapping("/users/{id}")
    public Result deleteUser(@PathVariable Long id, HttpServletRequest request) {
        Admin admin = getLoginAdmin(request); if (admin==null) return Result.error("无权限");
        if (admin.getRole()!=2) return Result.error("仅超级管理员可删除");
        sysUserService.removeById(id); return Result.success("删除成功");
    }
    // 笔记管理
    @GetMapping("/notes")
    public Result getNotes(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String keyword, @RequestParam(required=false) String type, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        LambdaQueryWrapper<Note> w = new LambdaQueryWrapper<>();
        if (keyword!=null && !keyword.isBlank()) w.like(Note::getTitle,keyword).or().like(Note::getAuthor,keyword);
        if (type!=null && !type.isBlank()) w.eq(Note::getType,type);
        w.orderByDesc(Note::getCreateTime);
        IPage<Note> r = noteService.page(new Page<>(page,size), w);
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }
    @DeleteMapping("/notes/{id}")
    public Result deleteNote(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        noteService.removeById(id);
        noteCommentService.remove(new LambdaQueryWrapper<NoteComment>().eq(NoteComment::getNoteId,id));
        return Result.success("删除成功");
    }
    @DeleteMapping("/comments/{id}")
    public Result deleteComment(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        noteCommentService.removeById(id); return Result.success("删除成功");
    }
    // 订单管理
    @GetMapping("/orders")
    public Result getOrders(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String keyword, @RequestParam(required=false) Integer status, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        LambdaQueryWrapper<SysOrder> w = new LambdaQueryWrapper<>();
        if (keyword!=null && !keyword.isBlank()) w.like(SysOrder::getUsername,keyword).or().like(SysOrder::getOrderNo,keyword);
        if (status!=null) w.eq(SysOrder::getPayStatus,status);
        w.orderByDesc(SysOrder::getCreateTime);
        IPage<SysOrder> r = orderService.page(new Page<>(page,size), w);
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }
    @PostMapping("/orders/{id}/ship")
    public Result shipOrder(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        SysOrder order = orderService.getById(id); if (order==null) return Result.error("订单不存在");
        if (order.getPayStatus()!=1) return Result.error("只有待发货订单才能发货");
        orderService.update(new LambdaUpdateWrapper<SysOrder>().eq(SysOrder::getId,id).set(SysOrder::getPayStatus,2));
        return Result.success("发货成功");
    }
    @PostMapping("/orders/{id}/status")
    public Result updateOrderStatus(@PathVariable Long id, @RequestBody Map<String,Integer> body, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        SysOrder order = orderService.getById(id); if (order==null) return Result.error("订单不存在");
        Integer status = body.get("status"); if (status==null) return Result.error("状态不能为空");
        orderService.update(new LambdaUpdateWrapper<SysOrder>().eq(SysOrder::getId,id).set(SysOrder::getPayStatus,status));
        return Result.success("状态已更新");
    }

    // ==================== 活动管理 ====================
    @GetMapping("/activities")
    public Result getActivities(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String keyword, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<hd> w = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) w.like(hd::getTitle, keyword);
        w.orderByAsc(hd::getId);
        IPage<hd> r = hdActivityMapper.selectPage(new Page<>(page, size), w);
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }

    @PostMapping("/activities/add")
    public Result addActivity(@RequestBody hd activity, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        hdActivityMapper.insert(activity);
        return Result.success("活动添加成功");
    }

    @DeleteMapping("/activities/{id}")
    public Result deleteActivity(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        hdActivityMapper.deleteById(id);
        return Result.success("活动删除成功");
    }

    @PostMapping("/activities/upload-img")
    public Result uploadActivityImg(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        if (file == null || file.isEmpty()) return Result.error("文件不能为空");
        try {
            java.io.File dir = new java.io.File("D:/project/hdimg/");
            if (!dir.exists()) dir.mkdirs();
            String original = file.getOriginalFilename();
            String suffix = original != null && original.contains(".") ? original.substring(original.lastIndexOf(".")) : ".jpg";
            String fileName = java.util.UUID.randomUUID() + suffix;
            file.transferTo(new java.io.File("D:/project/hdimg/" + fileName));
            return Result.success(fileName);
        } catch (Exception e) { return Result.error("上传失败：" + e.getMessage()); }
    }

    // ==================== 视频管理 ====================
    @GetMapping("/videos")
    public Result getVideos(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="10") int size,
            @RequestParam(required=false) String keyword, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        LambdaQueryWrapper<Video> w = new LambdaQueryWrapper<>();
        w.eq(Video::getIsDeleted, 0);
        if (keyword != null && !keyword.isBlank()) w.like(Video::getTitle, keyword).or().like(Video::getUser, keyword);
        w.orderByDesc(Video::getCreateTime);
        IPage<Video> r = videoService.page(new Page<>(page, size), w);
        Map<String,Object> data = new HashMap<>(); data.put("list",r.getRecords()); data.put("total",r.getTotal());
        return Result.success(data);
    }

    @DeleteMapping("/videos/{id}")
    public Result deleteVideo(@PathVariable Long id, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        Video video = videoService.getById(id);
        if (video == null) return Result.error("视频不存在");
        video.setIsDeleted(1);
        videoService.updateById(video);
        return Result.success("删除成功");
    }

    @PostMapping("/videos/{id}")
    public Result updateVideo(@PathVariable Long id, @RequestBody Video body, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        Video video = videoService.getById(id);
        if (video == null) return Result.error("视频不存在");
        if (body.getTitle() != null) video.setTitle(body.getTitle());
        if (body.getUser() != null) video.setUser(body.getUser());
        if (body.getCount() != null) video.setCount(body.getCount());
        if (body.getDate() != null) video.setDate(body.getDate());
        videoService.updateById(video);
        return Result.success("更新成功");
    }

    @PostMapping("/videos/upload-cover")
    public Result uploadVideoCover(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        if (file == null || file.isEmpty()) return Result.error("文件不能为空");
        try {
            java.io.File dir = new java.io.File("D:/project/videocover/"); if (!dir.exists()) dir.mkdirs();
            String suffix = file.getOriginalFilename(); suffix = suffix != null && suffix.contains(".") ? suffix.substring(suffix.lastIndexOf(".")) : ".jpg";
            String fileName = java.util.UUID.randomUUID() + suffix;
            file.transferTo(new java.io.File("D:/project/videocover/" + fileName));
            return Result.success(fileName);
        } catch (Exception e) { return Result.error("上传失败：" + e.getMessage()); }
    }

    @PostMapping("/videos/upload-file")
    public Result uploadVideoFile(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        if (file == null || file.isEmpty()) return Result.error("文件不能为空");
        try {
            java.io.File dir = new java.io.File("D:/project/video/"); if (!dir.exists()) dir.mkdirs();
            String suffix = file.getOriginalFilename(); suffix = suffix != null && suffix.contains(".") ? suffix.substring(suffix.lastIndexOf(".")) : ".mp4";
            String fileName = java.util.UUID.randomUUID() + suffix;
            file.transferTo(new java.io.File("D:/project/video/" + fileName));
            return Result.success(fileName);
        } catch (Exception e) { return Result.error("上传失败：" + e.getMessage()); }
    }

    @PostMapping("/videos/add")
    public Result addVideo(@RequestBody Video body, HttpServletRequest request) {
        if (notAdmin(request)) return Result.error("无权限");
        body.setIsDeleted(0);
        if (body.getCreateTime() == null) body.setCreateTime(java.time.LocalDateTime.now());
        videoService.save(body);
        return Result.success("视频添加成功");
    }
}