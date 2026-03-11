package com.example.kuromi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kuromi.common.Result;
import com.example.kuromi.entity.Note;
import com.example.kuromi.entity.NoteComment;
import com.example.kuromi.entity.SysUser;
import com.example.kuromi.service.NoteCommentService;
import com.example.kuromi.service.NoteService;
import com.example.kuromi.service.impl.SysUserService;
import com.example.kuromi.vo.NoteAllResponse;
import com.example.kuromi.vo.NoteLikeRequest;
import com.example.kuromi.vo.NoteLikeResponse;
import com.example.kuromi.vo.NoteListResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteCommentService noteCommentService;
    private final SysUserService sysUserService;

    @Value("${note.img.path:D:/project/noteimg/}")
    private String noteImgPath;

    // 根据用户名查询头像路径
    private String getAvatarByUsername(String username) {
        if (username == null || username.isEmpty()) return "";
        SysUser user = sysUserService.getOne(
            new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username).last("LIMIT 1")
        );
        return user != null && user.getUserImg() != null ? user.getUserImg() : "";
    }

    @GetMapping("/note-list")
    public NoteListResponse getNoteList() {
        return noteService.getNoteList();
    }

    @PostMapping("/note-like")
    public NoteLikeResponse updateNoteLike(@RequestBody NoteLikeRequest request) {
        return noteService.updateNoteLike(request);
    }

    @GetMapping("/note-all")
    public NoteAllResponse getAllNotes() {
        return noteService.getAllNotes();
    }

    @GetMapping("/note-detail")
    public Result getDetail(Long id, String type) {
        Note note = noteService.getOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getId, id)
                .eq(Note::getType, type));

        if (note == null) return Result.error("笔记不存在");

        List<String> imgsList = new ArrayList<>();
        if (note.getImgs() != null && !note.getImgs().trim().isEmpty()) {
            imgsList = Arrays.stream(note.getImgs().split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .collect(Collectors.toList());
        }

        List<NoteComment> comments = noteCommentService.getCommentsByNoteIdAndType(id, type);
        List<Map<String, Object>> commentList = comments.stream().map(comment -> {
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("id", comment.getId());
            commentMap.put("author", comment.getUsername());
            commentMap.put("content", comment.getContent());
            commentMap.put("createTime", comment.getCreateTime());
            commentMap.put("avatar", getAvatarByUsername(comment.getUsername()));
            return commentMap;
        }).toList();

        Map<String, Object> result = new HashMap<>();
        result.put("id", note.getId());
        result.put("title", note.getTitle());
        result.put("detail", note.getDetail());
        result.put("imgUrl", note.getImgUrl());
        result.put("imgs", note.getImgs());
        result.put("imgsList", imgsList);
        result.put("userImg", note.getUserImg());
        result.put("author", note.getAuthor());
        result.put("likes", note.getLikes());
        result.put("liked", note.getLiked());
        result.put("type", note.getType());
        result.put("createTime", note.getCreateTime());
        result.put("comments", commentList);
        return Result.success(result);
    }

    @PostMapping("/comment-add")
    public Result addComment(@RequestBody Map<String, Object> map) {
        try {
            if (map.get("noteId") == null || map.get("content") == null || map.get("type") == null) {
                return Result.error("笔记ID、评论内容、笔记类型不能为空");
            }
            String authorName = map.get("author") != null ? map.get("author").toString() : "游客";
            NoteComment comment = new NoteComment();
            comment.setNoteId(Long.valueOf(map.get("noteId").toString()));
            comment.setNoteType(map.get("type").toString());
            comment.setContent(map.get("content").toString().trim());
            comment.setUsername(authorName);
            comment.setUserId(0L);
            boolean saveSuccess = noteCommentService.save(comment);
            if (saveSuccess) {
                String avatar = getAvatarByUsername(authorName);
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("id", comment.getId());
                resultMap.put("author", comment.getUsername());
                resultMap.put("content", comment.getContent());
                resultMap.put("createTime", comment.getCreateTime());
                resultMap.put("avatar", avatar);
                return Result.success(resultMap);
            } else {
                return Result.error("评论发布失败");
            }
        } catch (NumberFormatException e) {
            return Result.error("参数格式错误，笔记ID必须为数字");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器异常，评论发布失败");
        }
    }

    /**
     * 上传笔记图片（可多张）
     * POST /api/community/upload-img
     */
    @PostMapping("/upload-img")
    public Result uploadNoteImg(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) return Result.error("文件不能为空");
        try {
            File dir = new File(noteImgPath);
            if (!dir.exists()) dir.mkdirs();
            String original = file.getOriginalFilename();
            String suffix = original != null && original.contains(".")
                ? original.substring(original.lastIndexOf(".")) : ".jpg";
            String fileName = UUID.randomUUID() + suffix;
            file.transferTo(new File(noteImgPath + fileName));
            // 返回可访问的相对路径（前端拼接 baseImgUrl）
            return Result.success(fileName);
        } catch (IOException e) {
            return Result.error("图片上传失败：" + e.getMessage());
        }
    }

    /**
     * 发布笔记
     * POST /api/community/publish
     */
    @PostMapping("/publish")
    public Result publishNote(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            // 校验登录
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("loginUser") == null) {
                return Result.error("请先登录");
            }
            SysUser loginUser = (SysUser) session.getAttribute("loginUser");

            String title = map.get("title") != null ? map.get("title").toString().trim() : "";
            String detail = map.get("detail") != null ? map.get("detail").toString().trim() : "";
            String type = map.get("type") != null ? map.get("type").toString().trim() : "gl";

            if (title.isEmpty()) return Result.error("标题不能为空");
            if (detail.isEmpty()) return Result.error("内容不能为空");

            // 图片列表（前端传文件名数组）
            List<String> imgs = new ArrayList<>();
            if (map.get("imgs") instanceof List) {
                imgs = ((List<?>) map.get("imgs")).stream()
                        .map(Object::toString).collect(Collectors.toList());
            }

            Note note = new Note();
            note.setContentId(0L);
            note.setTitle(title);
            note.setDetail(detail);
            note.setType(type);
            note.setAuthor(loginUser.getUsername());
            note.setUserImg(loginUser.getUserImg() != null ? loginUser.getUserImg() : "");
            note.setLikes(0);
            note.setLiked(false);
            // 主图为第一张
            note.setImgUrl(imgs.isEmpty() ? "" : imgs.get(0));
            // 其余图片逗号拼接
            note.setImgs(imgs.size() > 1 ? String.join(",", imgs.subList(1, imgs.size())) : "");

            boolean saved = noteService.save(note);
            if (saved) {
                return Result.success(note.getId());
            } else {
                return Result.error("发布失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器异常：" + e.getMessage());
        }
    }
}