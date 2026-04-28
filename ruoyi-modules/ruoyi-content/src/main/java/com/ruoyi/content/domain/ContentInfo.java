package com.ruoyi.content.domain;

import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * C-End Content Info Object
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public class ContentInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Content ID */
    private Long contentId;

    /** Content Title */
    @Excel(name = "Content Title")
    private String title;

    /** Content Summary */
    @Excel(name = "Content Summary")
    private String summary;

    /** Content Content */
    private String content;

    /** Category ID */
    @Excel(name = "Category ID")
    private Long categoryId;

    /** Author */
    @Excel(name = "Author")
    private String author;

    /** Cover Image */
    @Excel(name = "Cover Image")
    private String coverImage;

    /** Content Type (1-article 2-video 3-image 4-audio) */
    @Excel(name = "Content Type", readConverterExp = "1=article,2=video,3=image,4=audio")
    private String contentType;

    /** Content Status (0-published 1-pending 2-rejected) */
    @Excel(name = "Content Status", readConverterExp = "0=published,1=pending,2=rejected")
    private String status;

    /** View Count */
    @Excel(name = "View Count")
    private Long viewCount;

    /** Like Count */
    @Excel(name = "Like Count")
    private Long likeCount;

    /** Comment Count */
    @Excel(name = "Comment Count")
    private Long commentCount;

    /** Share Count */
    @Excel(name = "Share Count")
    private Long shareCount;

    /** Is Recommended (0-no 1-yes) */
    @Excel(name = "Is Recommended", readConverterExp = "0=no,1=yes")
    private String isRecommended;

    /** Is Top (0-no 1-yes) */
    @Excel(name = "Is Top", readConverterExp = "0=no,1=yes")
    private String isTop;

    /** Publish Time */
    private java.util.Date publishTime;

    /** Tags */
    @Excel(name = "Tags")
    private String tags;

    /** Source URL */
    @Excel(name = "Source URL")
    private String sourceUrl;

    private String categoryName;

    private String sortBy;

    private Long authorUserId;

    public String getSortBy()
    {
        return sortBy;
    }

    public void setSortBy(String sortBy)
    {
        this.sortBy = sortBy;
    }

    public Long getAuthorUserId()
    {
        return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId)
    {
        this.authorUserId = authorUserId;
    }

    public String getCategoryName()
    {
        return categoryName;
    }

    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public void setContentId(Long contentId) 
    {
        this.contentId = contentId;
    }

    public Long getContentId() 
    {
        return contentId;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setAuthor(String author) 
    {
        this.author = author;
    }

    public String getAuthor() 
    {
        return author;
    }

    public void setCoverImage(String coverImage) 
    {
        this.coverImage = coverImage;
    }

    public String getCoverImage() 
    {
        return coverImage;
    }

    public void setContentType(String contentType) 
    {
        this.contentType = contentType;
    }

    public String getContentType() 
    {
        return contentType;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setViewCount(Long viewCount) 
    {
        this.viewCount = viewCount;
    }

    public Long getViewCount() 
    {
        return viewCount;
    }

    public void setLikeCount(Long likeCount) 
    {
        this.likeCount = likeCount;
    }

    public Long getLikeCount() 
    {
        return likeCount;
    }

    public void setCommentCount(Long commentCount) 
    {
        this.commentCount = commentCount;
    }

    public Long getCommentCount() 
    {
        return commentCount;
    }

    public void setShareCount(Long shareCount) 
    {
        this.shareCount = shareCount;
    }

    public Long getShareCount() 
    {
        return shareCount;
    }

    public void setIsRecommended(String isRecommended) 
    {
        this.isRecommended = isRecommended;
    }

    public String getIsRecommended() 
    {
        return isRecommended;
    }

    public void setIsTop(String isTop) 
    {
        this.isTop = isTop;
    }

    public String getIsTop() 
    {
        return isTop;
    }

    public void setPublishTime(java.util.Date publishTime) 
    {
        this.publishTime = publishTime;
    }

    public java.util.Date getPublishTime() 
    {
        return publishTime;
    }

    public void setTags(String tags) 
    {
        this.tags = tags;
    }

    public String getTags() 
    {
        return tags;
    }

    public void setSourceUrl(String sourceUrl) 
    {
        this.sourceUrl = sourceUrl;
    }

    public String getSourceUrl() 
    {
        return sourceUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("contentId", getContentId())
            .append("title", getTitle())
            .append("summary", getSummary())
            .append("content", getContent())
            .append("categoryId", getCategoryId())
            .append("author", getAuthor())
            .append("coverImage", getCoverImage())
            .append("contentType", getContentType())
            .append("status", getStatus())
            .append("viewCount", getViewCount())
            .append("likeCount", getLikeCount())
            .append("commentCount", getCommentCount())
            .append("shareCount", getShareCount())
            .append("isRecommended", getIsRecommended())
            .append("isTop", getIsTop())
            .append("publishTime", getPublishTime())
            .append("tags", getTags())
            .append("sourceUrl", getSourceUrl())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
