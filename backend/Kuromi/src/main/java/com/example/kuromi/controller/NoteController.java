package com.example.kuromi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kuromi.common.Result;
import com.example.kuromi.entity.Note;
import com.example.kuromi.entity.NoteComment;
import com.example.kuromi.service.NoteCommentService;
import com.example.kuromi.service.NoteService;
import com.example.kuromi.vo.NoteAllResponse;
import com.example.kuromi.vo.NoteLikeRequest;
import com.example.kuromi.vo.NoteLikeResponse;
import com.example.kuromi.vo.NoteListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 笔记接口控制器
 * 路径完全对齐前端：/api/community/xxx
 */
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteCommentService noteCommentService; // 注入评论服务

    /**
     * 获取笔记列表
     * 对应前端：GET /api/community/note-list
     */
    @GetMapping("/note-list")
    public NoteListResponse getNoteList() {
        return noteService.getNoteList();
    }

    /**
     * 更新笔记点赞状态
     * 对应前端：POST /api/community/note-like
     */
    @PostMapping("/note-like")
    public NoteLikeResponse updateNoteLike(@RequestBody NoteLikeRequest request) {
        return noteService.updateNoteLike(request);
    }

    /**
     * 获取全部笔记（合并所有类型）
     * 对应前端：GET /api/community/note-all
     */
    @GetMapping("/note-all")
    public NoteAllResponse getAllNotes() {
        return noteService.getAllNotes();
    }

    /**
     * 笔记详情（关联查询评论列表）
     * 前端请求：GET /api/community/note-detail?id=xxx&type=xxx
     */
    @GetMapping("/note-detail")
    public Result getDetail(Long id, String type) {
        // 1. 查询笔记基本信息
        Note note = noteService.getOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getId, id)
                .eq(Note::getType, type));

        if (note == null) {
            return Result.error("笔记不存在");
        }

        // 2. 解析imgs：逗号字符串 → 数组（核心修复）
        List<String> imgsList = new ArrayList<>();
        if (note.getImgs() != null && !note.getImgs().trim().isEmpty()) {
            imgsList = Arrays.stream(note.getImgs().split(","))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .collect(Collectors.toList());
        }

        // 3. 处理评论
        List<NoteComment> comments = noteCommentService.getCommentsByNoteIdAndType(id, type);
        List<Map<String, Object>> commentList = comments.stream().map(comment -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", comment.getId());
            map.put("author", comment.getUsername());
            map.put("content", comment.getContent());
            map.put("createTime", comment.getCreateTime());
            map.put("avatar", "");
            return map;
        }).toList();

        // 4. 组装返回数据（含解析后的数组）
        Map<String, Object> result = new HashMap<>();
        result.put("id", note.getId());
        result.put("title", note.getTitle());
        result.put("detail", note.getDetail());
        result.put("imgUrl", note.getImgUrl()); // 主图
        result.put("imgs", note.getImgs());     // 原始字符串（保留）
        result.put("imgsList", imgsList);       // 解析后的数组（前端直接用）
        result.put("userImg", note.getUserImg());
        result.put("author", note.getAuthor());
        result.put("likes", note.getLikes());
        result.put("liked", note.getLiked());
        result.put("type", note.getType());
        result.put("createTime", note.getCreateTime());
        result.put("comments", commentList);

        return Result.success(result);
    }

    /**
     * 发表评论（适配NoteComment实体）
     * 前端请求参数：noteId、content、author（用户名）、type（笔记类型）
     */
    @PostMapping("/comment-add")
    public Result addComment(@RequestBody Map<String, Object> map) {
        try {
            // 1. 校验核心参数
            if (map.get("noteId") == null || map.get("content") == null || map.get("type") == null) {
                return Result.error("笔记ID、评论内容、笔记类型不能为空");
            }

            // 2. 构建评论实体
            NoteComment comment = new NoteComment();
            comment.setNoteId(Long.valueOf(map.get("noteId").toString()));
            comment.setNoteType(map.get("type").toString()); // 笔记类型（gl/tk/sp）
            comment.setContent(map.get("content").toString().trim()); // 去除首尾空格
            comment.setUsername(map.get("author") != null ? map.get("author").toString() : "游客"); // 用户名
            comment.setUserId(0L); // 若有用户登录，可替换为真实用户ID
            // createTime由MyBatisPlus自动填充，isDeleted默认0

            // 3. 保存评论到数据库
            boolean saveSuccess = noteCommentService.save(comment);

            if (saveSuccess) {
                // 4. 返回前端需要的评论信息（用于即时展示）
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("id", comment.getId());
                resultMap.put("author", comment.getUsername());
                resultMap.put("content", comment.getContent());
                resultMap.put("createTime", comment.getCreateTime());
                resultMap.put("avatar", "");
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
}
