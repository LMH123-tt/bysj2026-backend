package com.ruoyi.content.service;

import java.util.List;
import com.ruoyi.content.domain.ContentComment;

public interface IContentCommentService
{
    public List<ContentComment> selectContentCommentList(ContentComment contentComment);

    public ContentComment selectContentCommentById(Long commentId);

    public int insertContentComment(ContentComment contentComment);

    public int updateContentComment(ContentComment contentComment);

    public int deleteContentCommentByIds(Long[] commentIds);
}
