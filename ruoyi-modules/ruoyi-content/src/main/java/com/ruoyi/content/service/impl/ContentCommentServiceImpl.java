package com.ruoyi.content.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.content.mapper.ContentCommentMapper;
import com.ruoyi.content.domain.ContentComment;
import com.ruoyi.content.service.IContentCommentService;

@Service
public class ContentCommentServiceImpl implements IContentCommentService
{
    @Autowired
    private ContentCommentMapper contentCommentMapper;

    @Override
    public List<ContentComment> selectContentCommentList(ContentComment contentComment)
    {
        return contentCommentMapper.selectContentCommentList(contentComment);
    }

    @Override
    public ContentComment selectContentCommentById(Long commentId)
    {
        return contentCommentMapper.selectContentCommentById(commentId);
    }

    @Override
    public int insertContentComment(ContentComment contentComment)
    {
        return contentCommentMapper.insertContentComment(contentComment);
    }

    @Override
    public int updateContentComment(ContentComment contentComment)
    {
        return contentCommentMapper.updateContentComment(contentComment);
    }

    @Override
    public int deleteContentCommentByIds(Long[] commentIds)
    {
        return contentCommentMapper.deleteContentCommentByIds(commentIds);
    }
}
