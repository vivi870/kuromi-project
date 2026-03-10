package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.entity.Video;
import com.example.kuromi.mapper.VideoMapper;
import com.example.kuromi.service.VideoService;
import com.example.kuromi.vo.VideoDetailDTO;
import com.example.kuromi.vo.VideoListDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Override
    public List<VideoListDTO> getVideoList() {
        // 查询所有未删除的视频（按ID升序）
        List<Video> videoList = lambdaQuery()
                .eq(Video::getIsDeleted, 0)
                .orderByAsc(Video::getId)
                .list();

        // 转换为前端需要的DTO
        return videoList.stream().map(video -> {
            VideoListDTO dto = new VideoListDTO();
            BeanUtils.copyProperties(video, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public VideoDetailDTO getVideoById(Long id) {
        // 根据ID查询视频
        Video video = lambdaQuery()
                .eq(Video::getId, id)
                .eq(Video::getIsDeleted, 0)
                .one();

        if (video == null) {
            return null; // 未找到视频返回null
        }

        // 转换为详情DTO（注意videoFile映射为videoUrl）
        VideoDetailDTO dto = new VideoDetailDTO();
        BeanUtils.copyProperties(video, dto);
        dto.setVideoUrl(video.getVideoFile()); // 对齐前端的videoUrl字段
        return dto;
    }
}
