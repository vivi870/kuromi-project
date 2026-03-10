package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.NoteComment;

import java.util.List;

public interface NoteCommentService extends IService<NoteComment> {
    /**
     * 根据笔记ID和类型查询评论列表
     */
    List<NoteComment> getCommentsByNoteIdAndType(Long noteId, String noteType);
}
