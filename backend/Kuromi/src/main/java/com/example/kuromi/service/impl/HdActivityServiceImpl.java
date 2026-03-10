package com.example.kuromi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kuromi.mapper.HdActivityMapper;
import com.example.kuromi.entity.hd;
import com.example.kuromi.service.HdActivityService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HdActivityServiceImpl extends ServiceImpl<HdActivityMapper, hd> implements HdActivityService {

    private static final Logger log = LoggerFactory.getLogger(HdActivityServiceImpl.class);

    @Resource
    private HdActivityMapper hdActivityMapper;

    @Override
    public Page<hd> getHdPage(Integer pageNum, Integer pageSize, String keyword) {
        // 1. 校验分页参数（避免非法参数导致分页异常）
        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 10;

        // 2. 创建分页对象（核心：Page对象会自动计算偏移量）
        Page<hd> page = new Page<>(pageNum, pageSize);

        // 3. 构建查询条件（关键：删除字段别名，只查原始字段）
        QueryWrapper<hd> wrapper = new QueryWrapper<>();
        // 关键词模糊搜索（title字段）
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like("title", keyword.trim());
        }
        // 只查需要的字段（用原始数据库字段名，不要加别名）
        wrapper.orderByAsc("id");

        // 4. 执行分页查询（MP会自动拼接LIMIT，且正确计算count）
        hdActivityMapper.selectPage(page, wrapper);

        if (page.getRecords() != null && !page.getRecords().isEmpty()) {
            hd firstHd = page.getRecords().get(0);
            log.info("查询到的活动数据：id={}, title={}, time={}",
                    firstHd.getId(), firstHd.getTitle(), firstHd.getTime());
        }

        // 返回Page对象（已包含分页数据+总条数+总页数）
        return page;
    }

    /**
     * 新增：根据ID查询活动详情（核心：返回包含imgUrls的完整活动对象）
     */
    @Override
    public hd getHdDetailById(Long id) {
        // 1. 参数校验
        if (id == null || id < 1) {
            log.warn("活动详情查询失败：ID非法，id={}", id);
            return null;
        }

        // 2. 根据ID查询（MP自带的方法，自动映射所有字段，包括新增的imgUrls）
        hd hdDetail = hdActivityMapper.selectById(id);

        // 3. 日志打印
        if (hdDetail != null) {
            log.info("查询活动详情成功：id={}, imgUrls={}", hdDetail.getId(), hdDetail.getImgUrls());
        } else {
            log.warn("查询活动详情失败：未找到ID为{}的活动", id);
        }

        return hdDetail;
    }
}
