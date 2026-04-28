package com.ruoyi.content.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.content.domain.ContentLike;

public interface ContentLikeMapper
{
    public ContentLike selectByUserAndContent(@Param("userId") Long userId, @Param("contentId") Long contentId);

    public int insertContentLike(ContentLike contentLike);

    public int deleteByUserAndContent(@Param("userId") Long userId, @Param("contentId") Long contentId);
}
