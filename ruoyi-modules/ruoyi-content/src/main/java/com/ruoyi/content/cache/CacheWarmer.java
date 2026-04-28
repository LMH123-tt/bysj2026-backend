package com.ruoyi.content.cache;

import com.ruoyi.content.domain.ContentInfo;
import com.ruoyi.content.mapper.ContentInfoMapper;
import com.ruoyi.content.service.IContentInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CacheWarmer
{
    private static final Logger log = LoggerFactory.getLogger(CacheWarmer.class);

    @Autowired
    private MultiLevelCache multiLevelCache;

    @Autowired
    private IContentInfoService contentInfoService;

    @Autowired
    private ContentInfoMapper contentInfoMapper;

    @Autowired(required = false)
    private ContentBloomFilter bloomFilter;

    public void initBloomFilter()
    {
        try
        {
            log.info("Initializing bloom filter with existing content IDs");
            List<ContentInfo> allContent = contentInfoMapper.selectContentInfoList(new ContentInfo());
            if (allContent != null)
            {
                for (ContentInfo info : allContent)
                {
                    if (bloomFilter != null) bloomFilter.put(info.getContentId());
                }
                log.info("Bloom filter initialized with {} content IDs", allContent.size());
            }
        }
        catch (Exception e)
        {
            log.error("Failed to initialize bloom filter: {}", e.getMessage());
        }
    }

    @Async
    public void warmContentDetail(Long contentId)
    {
        try
        {
            log.info("预热内容详情缓存: contentId={}", contentId);
            multiLevelCache.get(
                    CacheConstants.CONTENT_DETAIL + contentId,
                    ContentInfo.class,
                    () -> contentInfoService.selectContentInfoByContentId(contentId),
                    CacheConstants.TTL_DETAIL,
                    CacheConstants.TTL_LOCAL_DETAIL
            );
        }
        catch (Exception e)
        {
            log.error("预热内容详情缓存失败: contentId={}, error={}", contentId, e.getMessage());
        }
    }

    @Async
    public void warmContentLists()
    {
        try
        {
            log.info("预热内容列表缓存");
            multiLevelCache.evictByPattern("list:*");
            contentInfoService.selectContentInfoListRecommended();
            contentInfoService.selectContentInfoListTop();
            contentInfoService.selectContentInfoListHot(10);
            contentInfoService.selectContentInfoListLatest(10);
        }
        catch (Exception e)
        {
            log.error("预热内容列表缓存失败: {}", e.getMessage());
        }
    }

    @Async
    public void warmCategoryCache()
    {
        try
        {
            log.info("预热分类缓存");
            multiLevelCache.evict(CacheConstants.CONTENT_CATEGORY);
        }
        catch (Exception e)
        {
            log.error("预热分类缓存失败: {}", e.getMessage());
        }
    }
}
