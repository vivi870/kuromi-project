package com.example.kuromi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.kuromi.common.Result;
import com.example.kuromi.entity.PageResult;
import com.example.kuromi.service.HdActivityService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.kuromi.entity.hd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mock") // 匹配Mock的路径前缀
public class HdController {

    private static final Logger log = LoggerFactory.getLogger(HdController.class);

    @Resource
    private HdActivityService hdActivityService;

    // 接口路径完全匹配：/mock/getHdList
    @GetMapping("/getHdList")
    public Result<PageResult<hd>> getHdList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "9") Integer pageSize,
            @RequestParam(required = false) String keyword
    ) {
        // 调用Service层查询分页数据
        Page<hd> page = hdActivityService.getHdPage(pageNum, pageSize, keyword);

        // 封装分页响应对象
        PageResult<hd> pageResult = new PageResult<>();
        pageResult.setRecords(page.getRecords());
        pageResult.setTotal(page.getTotal());
        pageResult.setCurrent((int) page.getCurrent());
        pageResult.setSize((int) page.getSize());

        return Result.success(pageResult);
    }

    @GetMapping("/getHdDetail")
    public Map<String, Object> getHdDetail(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();

        // 调用Service查询详情
        hd hdDetail = hdActivityService.getHdDetailById(id);

        if (hdDetail != null) {
            result.put("code", 200);
            result.put("msg", "success");
            result.put("id", hdDetail.getId());
            result.put("title", hdDetail.getTitle());
            result.put("time", hdDetail.getTime());
            result.put("imgUrl", hdDetail.getImgUrl()); // 原有列表图片

            // 核心：将逗号分隔的imgUrls转为数组，方便前端遍历
            String imgUrlsStr = hdDetail.getImgUrls();
            List<String> imgUrlsList = null;
            if (imgUrlsStr != null && !imgUrlsStr.trim().isEmpty()) {
                imgUrlsList = Arrays.asList(imgUrlsStr.split(","));
            }
            result.put("imgUrls", imgUrlsList == null ? List.of() : imgUrlsList);

            log.info("返回活动详情数据：id={}, imgUrls={}", id, imgUrlsList);
        } else {
            result.put("code", 500);
            result.put("msg", "未找到该活动");
            log.warn("返回活动详情失败：ID={}不存在", id);
        }

        return result;
    }
}
