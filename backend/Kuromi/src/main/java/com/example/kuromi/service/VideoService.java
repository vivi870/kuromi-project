package com.example.kuromi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kuromi.entity.Video;
import com.example.kuromi.vo.VideoDetailDTO;
import com.example.kuromi.vo.VideoListDTO;

import java.util.List;

public interface VideoService extends IService<Video> {
    // 获取视频列表
    List<VideoListDTO> getVideoList();

    // 根据ID获取单个视频详情
    VideoDetailDTO getVideoById(Long id);
}
