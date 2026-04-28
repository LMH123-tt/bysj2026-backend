package com.ruoyi.content.service.impl;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.content.mapper.ContentInfoMapper;
import com.ruoyi.content.mapper.ContentUserMapper;
import com.ruoyi.content.domain.ContentInfo;
import com.ruoyi.content.service.IContentInfoService;
import com.ruoyi.content.cache.MultiLevelCache;
import com.ruoyi.content.cache.CacheConstants;
import com.ruoyi.content.cache.ContentBloomFilter;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.github.pagehelper.PageHelper;

@Service
public class ContentInfoServiceImpl implements IContentInfoService
{
    @Autowired
    private ContentInfoMapper contentInfoMapper;

    @Autowired
    private ContentUserMapper contentUserMapper;

    @Autowired
    private MultiLevelCache multiLevelCache;

    @Autowired(required = false)
    private ContentBloomFilter bloomFilter;

    @Override
    public ContentInfo selectContentInfoByContentId(Long contentId)
    {
        return multiLevelCache.get(
                CacheConstants.CONTENT_DETAIL + contentId,
                ContentInfo.class,
                () -> contentInfoMapper.selectContentInfoByContentId(contentId),
                CacheConstants.TTL_DETAIL,
                CacheConstants.TTL_LOCAL_DETAIL
        );
    }

    @Override
    public List<ContentInfo> selectContentInfoList(ContentInfo contentInfo)
    {
        return contentInfoMapper.selectContentInfoList(contentInfo);
    }

    @Override
    public List<ContentInfo> selectContentInfoListPublished(ContentInfo contentInfo)
    {
        return contentInfoMapper.selectContentInfoListPublished(contentInfo);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContentInfo> selectContentInfoListRecommended()
    {
        return multiLevelCache.get(
                CacheConstants.CONTENT_RECOMMENDED,
                List.class,
                () -> {
                    PageHelper.clearPage();
                    return contentInfoMapper.selectContentInfoListRecommended();
                },
                CacheConstants.TTL_LIST,
                CacheConstants.TTL_LOCAL_LIST
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContentInfo> selectContentInfoListTop()
    {
        return multiLevelCache.get(
                CacheConstants.CONTENT_TOP,
                List.class,
                () -> {
                    PageHelper.clearPage();
                    return contentInfoMapper.selectContentInfoListTop();
                },
                CacheConstants.TTL_LIST,
                CacheConstants.TTL_LOCAL_LIST
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContentInfo> selectContentInfoListHot(Integer limit)
    {
        return multiLevelCache.get(
                CacheConstants.CONTENT_HOT + ":" + limit,
                List.class,
                () -> {
                    PageHelper.clearPage();
                    return contentInfoMapper.selectContentInfoListHot(limit);
                },
                CacheConstants.TTL_LIST,
                CacheConstants.TTL_LOCAL_LIST
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContentInfo> selectContentInfoListLatest(Integer limit)
    {
        return multiLevelCache.get(
                CacheConstants.CONTENT_LATEST + ":" + limit,
                List.class,
                () -> {
                    PageHelper.clearPage();
                    return contentInfoMapper.selectContentInfoListLatest(limit);
                },
                CacheConstants.TTL_LIST,
                CacheConstants.TTL_LOCAL_LIST
        );
    }

    @Override
    public List<ContentInfo> searchContentInfo(ContentInfo contentInfo)
    {
        String keyword = contentInfo.getTitle();
        if (keyword != null && !keyword.isEmpty())
        {
            String cacheKey = CacheConstants.CONTENT_SEARCH + keyword + ":cat" + contentInfo.getCategoryId();
            return multiLevelCache.get(
                    cacheKey,
                    List.class,
                    () -> contentInfoMapper.searchContentInfo(contentInfo),
                    CacheConstants.TTL_SEARCH,
                    10L
            );
        }
        return contentInfoMapper.searchContentInfo(contentInfo);
    }

    @Override
    public List<ContentInfo> selectContentInfoPersonalized(Long userId, Integer limit)
    {
        if (limit == null || limit <= 0)
        {
            limit = 10;
        }
        Set<ContentInfo> result = new LinkedHashSet<>();
        if (userId != null)
        {
            List<Long> viewedContentIds = contentInfoMapper.selectUserPreferredCategories(userId);
            if (viewedContentIds != null && !viewedContentIds.isEmpty())
            {
                Set<Long> categorySet = new LinkedHashSet<>();
                for (Long contentId : viewedContentIds)
                {
                    ContentInfo info = contentInfoMapper.selectContentInfoByContentId(contentId);
                    if (info != null && info.getCategoryId() != null)
                    {
                        categorySet.add(info.getCategoryId());
                        if (categorySet.size() >= 3)
                        {
                            break;
                        }
                    }
                }
                int perCategory = Math.max(2, limit / Math.max(categorySet.size(), 1));
                for (Long categoryId : categorySet)
                {
                    List<ContentInfo> categoryHot = contentInfoMapper.selectContentInfoByCategoryHot(categoryId, perCategory);
                    if (categoryHot != null)
                    {
                        result.addAll(categoryHot);
                    }
                }
            }
        }
        if (result.size() < limit)
        {
            PageHelper.clearPage();
            List<ContentInfo> hotList = contentInfoMapper.selectContentInfoListHot(limit);
            if (hotList != null)
            {
                result.addAll(hotList);
            }
        }
        List<ContentInfo> finalList = new ArrayList<>(result);
        if (finalList.size() > limit)
        {
            finalList = finalList.subList(0, limit);
        }
        return finalList;
    }

    @Override
    public int insertContentInfo(ContentInfo contentInfo)
    {
        int result = contentInfoMapper.insertContentInfo(contentInfo);
        if (result > 0)
        {
            if (bloomFilter != null) bloomFilter.put(contentInfo.getContentId());
            multiLevelCache.evictByPattern("list:*");
            multiLevelCache.evictByPattern("search:*");
        }
        return result;
    }

    @Override
    public int updateContentInfo(ContentInfo contentInfo)
    {
        int result = contentInfoMapper.updateContentInfo(contentInfo);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_DETAIL + contentInfo.getContentId());
            multiLevelCache.evictByPattern("list:*");
            multiLevelCache.evictByPattern("search:*");
        }
        return result;
    }

    @Override
    public int updateContentViewCount(Long contentId)
    {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                params.put("contentId", contentId);
                contentUserMapper.insertViewHistory(params);
            }
        } catch (Exception ignored) {}
        return contentInfoMapper.updateContentViewCount(contentId);
    }

    @Override
    public int updateContentLikeCount(Long contentId, Integer increment)
    {
        int result = contentInfoMapper.updateContentLikeCount(contentId, increment);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_DETAIL + contentId);
        }
        return result;
    }

    @Override
    public int updateContentCommentCount(Long contentId)
    {
        int result = contentInfoMapper.updateContentCommentCount(contentId);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_DETAIL + contentId);
        }
        return result;
    }

    @Override
    public int decrementContentCommentCount(Long contentId)
    {
        int result = contentInfoMapper.decrementContentCommentCount(contentId);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_DETAIL + contentId);
        }
        return result;
    }

    @Override
    public int deleteContentInfoByContentIds(Long[] contentIds)
    {
        int result = contentInfoMapper.deleteContentInfoByContentIds(contentIds);
        if (result > 0)
        {
            for (Long id : contentIds)
            {
                multiLevelCache.evict(CacheConstants.CONTENT_DETAIL + id);
            }
            multiLevelCache.evictByPattern("list:*");
            multiLevelCache.evictByPattern("search:*");
        }
        return result;
    }

    @Override
    public int deleteContentInfoByContentId(Long contentId)
    {
        int result = contentInfoMapper.deleteContentInfoByContentId(contentId);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_DETAIL + contentId);
            multiLevelCache.evictByPattern("list:*");
            multiLevelCache.evictByPattern("search:*");
        }
        return result;
    }
}
