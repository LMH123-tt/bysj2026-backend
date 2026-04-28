package com.ruoyi.content.service;

import java.util.List;
import com.ruoyi.content.domain.ContentInfo;

/**
 * C-End Content Info Service Interface
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public interface IContentInfoService 
{
    /**
     * Query Content Info
     * 
     * @param contentId Content Info primary key
     * @return Content Info
     */
    public ContentInfo selectContentInfoByContentId(Long contentId);

    /**
     * Query Content Info List
     * 
     * @param contentInfo Content Info
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoList(ContentInfo contentInfo);

    /**
     * Query Published Content Info List
     * 
     * @param contentInfo Content Info
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoListPublished(ContentInfo contentInfo);

    /**
     * Query Recommended Content Info List
     * 
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoListRecommended();

    /**
     * Query Top Content Info List
     * 
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoListTop();

    /**
     * Query Hot Content Info List
     * 
     * @param limit limit count
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoListHot(Integer limit);

    /**
     * Query Latest Content Info List
     * 
     * @param limit limit count
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoListLatest(Integer limit);

    public List<ContentInfo> searchContentInfo(ContentInfo contentInfo);

    public List<ContentInfo> selectContentInfoPersonalized(Long userId, Integer limit);

    /**
     * Add Content Info
     * 
     * @param contentInfo Content Info
     * @return result
     */
    public int insertContentInfo(ContentInfo contentInfo);

    /**
     * Update Content Info
     * 
     * @param contentInfo Content Info
     * @return result
     */
    public int updateContentInfo(ContentInfo contentInfo);

    /**
     * Update Content View Count
     * 
     * @param contentId Content Info primary key
     * @return result
     */
    public int updateContentViewCount(Long contentId);

    /**
     * Update Content Like Count
     * 
     * @param contentId Content Info primary key
     * @param increment increment count
     * @return result
     */
    public int updateContentLikeCount(Long contentId, Integer increment);

    public int updateContentCommentCount(Long contentId);

    public int decrementContentCommentCount(Long contentId);

    /**
     * Batch Delete Content Infos
     * 
     * @param contentIds Content Info primary keys collection
     * @return result
     */
    public int deleteContentInfoByContentIds(Long[] contentIds);

    /**
     * Delete Content Info
     * 
     * @param contentId Content Info primary key
     * @return result
     */
    public int deleteContentInfoByContentId(Long contentId);
}
