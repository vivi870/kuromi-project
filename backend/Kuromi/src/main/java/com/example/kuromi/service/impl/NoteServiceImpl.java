package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.entity.Note;
import com.example.kuromi.mapper.NoteMapper;
import com.example.kuromi.service.NoteService;
import com.example.kuromi.vo.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * 笔记业务层实现
 */
@Service
@RequiredArgsConstructor
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);
    private final NoteMapper noteMapper;

    // 移除图片基础路径常量（不再使用）

    @Override
    public NoteListResponse getNoteList() {
        // 查询不同类型的笔记
        List<Note> glList = noteMapper.selectByType("gl");
        List<Note> tkList = noteMapper.selectByType("tk");
        List<Note> spList = noteMapper.selectByType("sp");

        // 移除图片URL拼接逻辑（直接返回数据库原始值）

        // 构建返回结果
        NoteListResponse response = new NoteListResponse();
        response.setCode(200);
        response.setMessage("success");
        response.setData(new NoteListResponse.NoteData(glList, tkList, spList));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoteLikeResponse updateNoteLike(NoteLikeRequest request) {
        NoteLikeResponse response = new NoteLikeResponse();

        // 参数校验
        if (request.getNoteId() == null || !StringUtils.hasText(request.getType())) {
            response.setCode(400);
            response.setMessage("参数错误");
            response.setData(null);
            return response;
        }

        // 查询目标笔记
        Note note = getById(request.getNoteId());
        if (note == null || !note.getType().equals(request.getType())) {
            response.setCode(404);
            response.setMessage("笔记不存在");
            response.setData(null);
            return response;
        }

        // 更新点赞状态和点赞数
        boolean isLiked = request.getIsLiked();
        note.setLiked(isLiked);
        note.setLikes(isLiked ? note.getLikes() + 1 : Math.max(0, note.getLikes() - 1));
        updateById(note);

        // 构建返回数据
        NoteLikeResponse.NoteLikeData data = new NoteLikeResponse.NoteLikeData();
        data.setId(request.getNoteId().toString());
        data.setType(request.getType());
        data.setLiked(isLiked);
        data.setLikes(note.getLikes());

        response.setCode(200);
        response.setMessage(isLiked ? "点赞成功" : "取消点赞成功");
        response.setData(data);
        return response;
    }


    /**
     * 获取全部笔记（合并所有类型）
     */
    @Override
    public NoteAllResponse getAllNotes() {
        NoteAllResponse response = new NoteAllResponse();

        try {
            // 查询所有笔记（不分类型）
            QueryWrapper<Note> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id"); // 按ID倒序排列
            List<Note> allNotes = noteMapper.selectList(wrapper);

            // 移除图片URL拼接逻辑

            // 构建返回结果
            NoteAllResponse.NoteAllData data = new NoteAllResponse.NoteAllData();
            data.setTotal(allNotes.size()); // 总笔记数
            data.setList(allNotes); // 全部笔记列表

            response.setCode(200);
            response.setMessage("success");
            response.setData(data);
        } catch (Exception e) {
            // 异常处理
            response.setCode(500);
            response.setMessage("获取全部笔记失败");
            response.setData(new NoteAllResponse.NoteAllData()); // 兜底空数据
            log.error("获取全部笔记异常：", e);
        }

        return response;
    }

    @Override
    public NoteDetailResponse getNoteDetail(Long id, String type) {
        NoteDetailResponse response = new NoteDetailResponse();

        try {
            // 1. 参数校验
            if (id == null || !StringUtils.hasText(type)) {
                response.setCode(400);
                response.setMessage("笔记ID或类型不能为空");
                response.setData(null);
                return response;
            }

            // 2. 查询笔记详情（按ID+类型精准查询）
            QueryWrapper<Note> wrapper = new QueryWrapper<>();
            wrapper.eq("id", id)
                    .eq("type", type);
            Note note = noteMapper.selectOne(wrapper);

            // 3. 笔记不存在的处理
            if (note == null) {
                response.setCode(404);
                response.setMessage("笔记不存在");
                response.setData(null);
                return response;
            }

            // 4. 构建返回数据（包含detail、createTime等字段）
            NoteDetailResponse.NoteDetailData data = new NoteDetailResponse.NoteDetailData();
            // 复制Note实体的所有字段（包括新增的detail、createTime）
            data.setId(note.getId());
            data.setTitle(note.getTitle());
            data.setContentId(String.valueOf(note.getContentId()));
            data.setDetail(note.getDetail()); // 新增的笔记详情字段
            data.setType(note.getType());
            data.setAuthor(note.getAuthor());
            data.setLikes(note.getLikes());
            data.setLiked(note.getLiked());
            data.setImgUrl(note.getImgUrl());
            data.setUserimg(note.getUserImg());
            data.setCreateTime(note.getCreateTime()); // 发布时间
            data.setUpdateTime(note.getUpdateTime());

            // 5. 组装返回结果
            response.setCode(200);
            response.setMessage("success");
            response.setData(data);
        } catch (Exception e) {
            // 异常兜底
            response.setCode(500);
            response.setMessage("获取笔记详情失败");
            response.setData(null);
            log.error("获取笔记详情异常（ID：{}，类型：{}）：", id, type, e);
        }

        return response;
    }
}
