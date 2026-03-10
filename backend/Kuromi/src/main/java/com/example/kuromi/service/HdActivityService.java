package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.hd;

public interface HdActivityService extends IService<hd> {
    // 声明分页+搜索方法
    Page<hd> getHdPage(Integer pageNum, Integer pageSize, String keyword);

    hd getHdDetailById(Long id);
}
