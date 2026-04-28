package com.ruoyi.content.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContentBlockHandler
{
    private static final Logger log = LoggerFactory.getLogger(ContentBlockHandler.class);

    public static AjaxResult handleBlock(BlockException ex)
    {
        log.warn("请求被限流: {}", ex.getRule());
        return AjaxResult.error("当前访问人数过多，请稍后再试");
    }

    public static TableDataInfo handleBlockSearch(String keyword, Long categoryId, String contentType, BlockException ex)
    {
        log.warn("搜索请求被限流: {}", ex.getRule());
        TableDataInfo info = new TableDataInfo();
        info.setRows(new ArrayList<>());
        info.setTotal(0);
        info.setMsg("当前访问人数过多，请稍后再试");
        return info;
    }

    public static AjaxResult handleFallback(Long contentId, Throwable throwable)
    {
        log.error("内容详情服务异常，返回兜底数据: {}", throwable.getMessage());
        Map<String, Object> data = new HashMap<>();
        data.put("contentId", contentId);
        data.put("title", "服务暂时不可用");
        data.put("summary", "内容服务正在维护中，请稍后再试");
        data.put("content", "系统繁忙，请稍后重试");
        data.put("contentType", "1");
        data.put("status", "0");
        return AjaxResult.success(data);
    }

    public static AjaxResult handleFallback(Throwable throwable)
    {
        log.error("内容服务异常，返回兜底数据: {}", throwable.getMessage());
        return AjaxResult.success(new ArrayList<>());
    }

    public static TableDataInfo handleFallbackSearch(String keyword, Long categoryId, String contentType, Throwable throwable)
    {
        log.error("搜索服务异常，返回兜底数据: {}", throwable.getMessage());
        TableDataInfo info = new TableDataInfo();
        info.setRows(new ArrayList<>());
        info.setTotal(0);
        info.setMsg("搜索服务暂时不可用");
        return info;
    }
}
