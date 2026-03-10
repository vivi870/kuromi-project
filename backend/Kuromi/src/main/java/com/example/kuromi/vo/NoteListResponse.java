package com.example.kuromi.vo;

import com.example.kuromi.entity.Note;
import lombok.Data;

import java.util.List;

/**
 * 笔记列表返回结果
 */
@Data
public class NoteListResponse {
    private Integer code;
    private String message;
    private NoteData data;

    @Data
    public static class NoteData {
        private List<Note> gllist;
        private List<Note> tklist;
        private List<Note> splist;

        public NoteData(List<Note> gllist, List<Note> tklist, List<Note> splist) {
            this.gllist = gllist;
            this.tklist = tklist;
            this.splist = splist;
        }
    }
}
