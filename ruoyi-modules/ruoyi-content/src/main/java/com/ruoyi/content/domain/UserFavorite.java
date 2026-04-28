package com.ruoyi.content.domain;

import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * C-End User Favorite Object
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public class UserFavorite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Favorite ID */
    private Long favoriteId;

    /** User ID */
    @Excel(name = "User ID")
    private Long userId;

    /** Content ID */
    @Excel(name = "Content ID")
    private Long contentId;

    /** Favorite Time */
    @Excel(name = "Favorite Time")
    private java.util.Date favoriteTime;

    public void setFavoriteId(Long favoriteId) 
    {
        this.favoriteId = favoriteId;
    }

    public Long getFavoriteId() 
    {
        return favoriteId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setContentId(Long contentId) 
    {
        this.contentId = contentId;
    }

    public Long getContentId() 
    {
        return contentId;
    }

    public void setFavoriteTime(java.util.Date favoriteTime) 
    {
        this.favoriteTime = favoriteTime;
    }

    public java.util.Date getFavoriteTime() 
    {
        return favoriteTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("favoriteId", getFavoriteId())
            .append("userId", getUserId())
            .append("contentId", getContentId())
            .append("favoriteTime", getFavoriteTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
