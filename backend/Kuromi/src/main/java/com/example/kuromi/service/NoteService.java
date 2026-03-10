package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.Note;
import com.example.kuromi.vo.*;

/**
 * 笔记业务层接口
 */
public interface NoteService extends IService<Note> {

    /**
     * 获取笔记列表（按类型分组）
     * @return 分组后的笔记列表
     */
    NoteListResponse getNoteList();

    /**
     * 更新笔记点赞状态
     * @param request 点赞请求参数
     * @return 点赞结果
     */
    NoteLikeResponse updateNoteLike(NoteLikeRequest request);

    /**
     * 获取全部笔记（合并所有类型）
     * @return 全部笔记列表
     */
    NoteAllResponse getAllNotes();

    NoteDetailResponse getNoteDetail(Long id, String type);
}
