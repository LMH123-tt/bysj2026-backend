package com.ruoyi.content.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.content.domain.ContentInfo;

/**
 * C-End Content Info Mapper Interface
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public interface ContentInfoMapper 
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

    public List<ContentInfo> selectContentInfoByCategoryHot(@Param("categoryId") Long categoryId, @Param("limit") Integer limit);

    public List<Long> selectUserPreferredCategories(@Param("userId") Long userId);

    /**
     * Query Latest Content Info List
     * 
     * @param limit limit count
     * @return Content Info collection
     */
    public List<ContentInfo> selectContentInfoListLatest(Integer limit);

    public List<ContentInfo> searchContentInfo(ContentInfo contentInfo);

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

    public int updateContentShareCount(Long contentId);

    /**
     * Delete Content Info
     * 
     * @param contentId Content Info primary key
     * @return result
     */
    public int deleteContentInfoByContentId(Long contentId);

    /**
     * Delete Content Infos
     * 
     * @param contentIds Content Info primary keys collection
     * @return result
     */
    public int deleteContentInfoByContentIds(Long[] contentIds);

    public Map<String, Object> selectContentStats();

    public List<Map<String, Object>> selectContentTypeStats();

    public List<Map<String, Object>> selectRecentContentStats();

    public List<ContentInfo> selectContentRanking(@Param("categoryId") Long categoryId, @Param("period") String period, @Param("limit") Integer limit);

    public List<ContentInfo> selectRelatedContent(@Param("excludeId") Long excludeId, @Param("categoryId") Long categoryId, @Param("limit") Integer limit);

    public List<Map<String, Object>> selectHotTags();

    public int countByUserId(@Param("userId") Long userId);
}
