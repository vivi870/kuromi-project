package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.entity.NoteComment;
import com.example.kuromi.mapper.NoteCommentMapper;
import com.example.kuromi.service.NoteCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteCommentServiceImpl extends ServiceImpl<NoteCommentMapper, NoteComment> implements NoteCommentService {

    /**
     * 根据笔记ID和类型查询评论列表（按创建时间倒序）
     */
    @Override
    public List<NoteComment> getCommentsByNoteIdAndType(Long noteId, String noteType) {
        LambdaQueryWrapper<NoteComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoteComment::getNoteId, noteId)
                .eq(NoteComment::getNoteType, noteType)
                .eq(NoteComment::getIsDeleted, 0) // 排除已删除的评论
                .orderByDesc(NoteComment::getCreateTime); // 最新评论在前
        return this.list(wrapper);
    }
}
