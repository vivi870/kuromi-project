package com.example.kuromi.controller;

import com.example.kuromi.service.VideoService;
import com.example.kuromi.vo.VideoDetailDTO;
import com.example.kuromi.vo.VideoListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    /**
     * 获取视频列表接口（对齐前端Mock的/mock/getVideoList）
     * 实际访问路径：http://localhost:8080/api/getVideoList
     */
    @GetMapping("/getVideoList")
    public Map<String, Object> getVideoList() {
        List<VideoListDTO> videoList = videoService.getVideoList();
        // 返回和前端Mock完全一致的格式（code/message/data）
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", videoList);
        return result;
    }

    /**
     * 获取单个视频详情接口（对齐前端Mock的/mock/getVideo）
     * 实际访问路径：http://localhost:8080/api/getVideo?id=1
     */
    @GetMapping("/getVideo")
    public Map<String, Object> getVideo(@RequestParam Long id) {
        VideoDetailDTO video = videoService.getVideoById(id);
        Map<String, Object> result = new HashMap<>();

        if (video != null) {
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", video);
        } else {
            result.put("code", 404);
            result.put("message", "视频不存在");
            result.put("data", null);
        }
        return result;
    }
}
