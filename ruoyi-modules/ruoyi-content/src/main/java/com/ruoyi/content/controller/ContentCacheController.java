package com.ruoyi.content.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.content.cache.MultiLevelCache;
import com.ruoyi.content.cache.MultiLevelCache.LocalCacheStats;
import com.ruoyi.content.cache.CacheConstants;

@RestController
@RequestMapping("/content/cache")
public class ContentCacheController extends BaseController
{
    @Autowired
    private MultiLevelCache multiLevelCache;

    @Autowired
    private RedisService redisService;

    @GetMapping("/stats")
    public AjaxResult stats()
    {
        Map<String, Object> result = new HashMap<>();
        LocalCacheStats l1Stats = multiLevelCache.getStats();
        result.put("l1HitCount", l1Stats.getHitCount());
        result.put("l1MissCount", l1Stats.getMissCount());
        result.put("l1HitRate", String.format("%.2f%%", l1Stats.getHitRate() * 100));
        result.put("l1Size", l1Stats.getSize());
        result.put("l1EvictionCount", l1Stats.getEvictionCount());

        Collection<String> l2Keys = redisService.keys("content:cache:*");
        int l2KeyCount = (l2Keys != null) ? l2Keys.size() : 0;
        result.put("l2KeyCount", l2KeyCount);

        int detailCount = 0, listCount = 0, searchCount = 0, categoryCount = 0;
        if (l2Keys != null)
        {
            for (String key : l2Keys)
            {
                if (key.contains("detail:")) detailCount++;
                else if (key.contains("list:")) listCount++;
                else if (key.contains("search:")) searchCount++;
                else if (key.contains("category:")) categoryCount++;
            }
        }
        result.put("l2DetailCount", detailCount);
        result.put("l2ListCount", listCount);
        result.put("l2SearchCount", searchCount);
        result.put("l2CategoryCount", categoryCount);

        return success(result);
    }

    @DeleteMapping("/clear")
    public AjaxResult clear()
    {
        multiLevelCache.clear();
        return success("缓存已清理");
    }

    @DeleteMapping("/clear/detail")
    public AjaxResult clearDetail()
    {
        multiLevelCache.evictByPattern(CacheConstants.CONTENT_DETAIL + "*");
        return success("内容详情缓存已清理");
    }

    @DeleteMapping("/clear/list")
    public AjaxResult clearList()
    {
        multiLevelCache.evictByPattern("list:*");
        return success("列表缓存已清理");
    }

    @DeleteMapping("/clear/search")
    public AjaxResult clearSearch()
    {
        multiLevelCache.evictByPattern("search:*");
        return success("搜索缓存已清理");
    }

    @DeleteMapping("/clear/category")
    public AjaxResult clearCategory()
    {
        multiLevelCache.evict(CacheConstants.CONTENT_CATEGORY);
        return success("分类缓存已清理");
    }
}
