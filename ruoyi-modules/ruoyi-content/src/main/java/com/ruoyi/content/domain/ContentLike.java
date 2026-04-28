package com.ruoyi.content.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class ContentLike extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long likeId;
    private Long contentId;
    private Long userId;

    public Long getLikeId()
    {
        return likeId;
    }

    public void setLikeId(Long likeId)
    {
        this.likeId = likeId;
    }

    public Long getContentId()
    {
        return contentId;
    }

    public void setContentId(Long contentId)
    {
        this.contentId = contentId;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }
}
