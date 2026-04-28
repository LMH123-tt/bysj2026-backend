package com.ruoyi.content.service;

import java.util.List;
import com.ruoyi.content.domain.ContentCategory;

/**
 * C-End Content Category Service Interface
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public interface IContentCategoryService 
{
    /**
     * Query Content Category
     * 
     * @param categoryId Content Category primary key
     * @return Content Category
     */
    public ContentCategory selectContentCategoryByCategoryId(Long categoryId);

    /**
     * Query Content Category List
     * 
     * @param contentCategory Content Category
     * @return Content Category collection
     */
    public List<ContentCategory> selectContentCategoryList(ContentCategory contentCategory);

    /**
     * Query Enabled Content Category List
     * 
     * @return Content Category collection
     */
    public List<ContentCategory> selectContentCategoryListEnabled();

    /**
     * Add Content Category
     * 
     * @param contentCategory Content Category
     * @return result
     */
    public int insertContentCategory(ContentCategory contentCategory);

    /**
     * Update Content Category
     * 
     * @param contentCategory Content Category
     * @return result
     */
    public int updateContentCategory(ContentCategory contentCategory);

    /**
     * Batch Delete Content Categories
     * 
     * @param categoryIds Content Category primary keys collection
     * @return result
     */
    public int deleteContentCategoryByCategoryIds(Long[] categoryIds);

    /**
     * Delete Content Category
     * 
     * @param categoryId Content Category primary key
     * @return result
     */
    public int deleteContentCategoryByCategoryId(Long categoryId);
}
