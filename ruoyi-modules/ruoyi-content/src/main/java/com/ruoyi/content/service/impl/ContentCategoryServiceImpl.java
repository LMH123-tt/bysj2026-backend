package com.ruoyi.content.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.content.mapper.ContentCategoryMapper;
import com.ruoyi.content.domain.ContentCategory;
import com.ruoyi.content.service.IContentCategoryService;
import com.ruoyi.content.cache.MultiLevelCache;
import com.ruoyi.content.cache.CacheConstants;

@Service
public class ContentCategoryServiceImpl implements IContentCategoryService
{
    @Autowired
    private ContentCategoryMapper contentCategoryMapper;

    @Autowired
    private MultiLevelCache multiLevelCache;

    @Override
    public ContentCategory selectContentCategoryByCategoryId(Long categoryId)
    {
        return contentCategoryMapper.selectContentCategoryByCategoryId(categoryId);
    }

    @Override
    public List<ContentCategory> selectContentCategoryList(ContentCategory contentCategory)
    {
        return contentCategoryMapper.selectContentCategoryList(contentCategory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ContentCategory> selectContentCategoryListEnabled()
    {
        return multiLevelCache.get(
                CacheConstants.CONTENT_CATEGORY,
                List.class,
                () -> contentCategoryMapper.selectContentCategoryListEnabled(),
                CacheConstants.TTL_CATEGORY,
                CacheConstants.TTL_LOCAL_CATEGORY
        );
    }

    @Override
    public int insertContentCategory(ContentCategory contentCategory)
    {
        int result = contentCategoryMapper.insertContentCategory(contentCategory);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_CATEGORY);
        }
        return result;
    }

    @Override
    public int updateContentCategory(ContentCategory contentCategory)
    {
        int result = contentCategoryMapper.updateContentCategory(contentCategory);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_CATEGORY);
        }
        return result;
    }

    @Override
    public int deleteContentCategoryByCategoryIds(Long[] categoryIds)
    {
        int result = contentCategoryMapper.deleteContentCategoryByCategoryIds(categoryIds);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_CATEGORY);
        }
        return result;
    }

    @Override
    public int deleteContentCategoryByCategoryId(Long categoryId)
    {
        int result = contentCategoryMapper.deleteContentCategoryByCategoryId(categoryId);
        if (result > 0)
        {
            multiLevelCache.evict(CacheConstants.CONTENT_CATEGORY);
        }
        return result;
    }
}
