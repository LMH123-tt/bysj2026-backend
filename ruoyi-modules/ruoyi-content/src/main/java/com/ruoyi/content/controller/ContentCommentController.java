package com.ruoyi.content.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.content.domain.ContentComment;
import com.ruoyi.content.service.IContentCommentService;
import com.ruoyi.content.service.IContentInfoService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/content/comment")
public class ContentCommentController extends BaseController
{
    @Autowired
    private IContentCommentService contentCommentService;

    @Autowired
    private IContentInfoService contentInfoService;

    @GetMapping("/list")
    public TableDataInfo list(ContentComment contentComment)
    {
        startPage();
        List<ContentComment> list = contentCommentService.selectContentCommentList(contentComment);
        return getDataTable(list);
    }

    @GetMapping("/{commentId}")
    public AjaxResult getInfo(@PathVariable("commentId") Long commentId)
    {
        return success(contentCommentService.selectContentCommentById(commentId));
    }

    @PostMapping
    public AjaxResult add(@RequestBody ContentComment contentComment)
    {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                contentComment.setUserId(userId);
            }
        } catch (Exception ignored) {}
        contentComment.setStatus("0");
        int rows = contentCommentService.insertContentComment(contentComment);
        if (rows > 0) {
            contentInfoService.updateContentCommentCount(contentComment.getContentId());
        }
        return toAjax(rows);
    }

    @RequiresPermissions("content:comment:edit")
    @Log(title = "内容评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContentComment contentComment)
    {
        return toAjax(contentCommentService.updateContentComment(contentComment));
    }

    @Log(title = "内容评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{commentIds}")
    public AjaxResult remove(@PathVariable Long[] commentIds)
    {
        for (Long commentId : commentIds)
        {
            ContentComment comment = contentCommentService.selectContentCommentById(commentId);
            if (comment != null && comment.getContentId() != null)
            {
                contentInfoService.decrementContentCommentCount(comment.getContentId());
            }
        }
        return toAjax(contentCommentService.deleteContentCommentByIds(commentIds));
    }
}
