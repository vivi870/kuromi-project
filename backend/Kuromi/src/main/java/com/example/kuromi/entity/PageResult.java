package com.example.kuromi.entity;

import lombok.Data;

import java.util.List;

/**
 * 分页响应对象（适配前端分页组件）
 */
@Data
public class PageResult<T> {
    private List<T> records; // 当前页数据
    private long total;      // 总页数
    private int current;     // 当前页
    private int size;        // 页大小
}
