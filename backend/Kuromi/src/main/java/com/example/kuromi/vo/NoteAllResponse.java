package com.example.kuromi.vo;

import com.example.kuromi.entity.Note;
import lombok.Data;

import java.util.List;

/**
 * 全部笔记返回结果
 */
@Data
public class NoteAllResponse {
    private Integer code;
    private String message;
    private NoteAllData data;

    @Data
    public static class NoteAllData {
        private Integer total; // 总笔记数
        private List<Note> list; // 全部笔记列表
    }
}
