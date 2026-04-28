package com.ruoyi.content.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ruoyi.content.handler.ContentBlockHandler;
import com.ruoyi.content.cache.CacheWarmer;
import com.ruoyi.content.domain.ContentInfo;
import com.ruoyi.content.service.IContentInfoService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * C-End Content Info Controller
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
@RestController
@RequestMapping("/content/info")
public class ContentInfoController extends BaseController
{
    @Autowired
    private IContentInfoService contentInfoService;

    @Autowired
    private CacheWarmer cacheWarmer;

    @Autowired
    private com.ruoyi.content.mapper.ContentInfoMapper contentInfoMapper;

    @Autowired
    private com.ruoyi.content.mapper.ContentLikeMapper contentLikeMapper;

    @Autowired
    private com.ruoyi.content.mapper.ContentUserMapper contentUserMapper;

    /**
     * Query Content Info List
     */
    @RequiresPermissions("content:info:list")
    @GetMapping("/list")
    public TableDataInfo list(ContentInfo contentInfo)
    {
        startPage();
        List<ContentInfo> list = contentInfoService.selectContentInfoList(contentInfo);
        return getDataTable(list);
    }

    /**
     * Query Published Content Info List
     */
    @GetMapping("/published")
    @SentinelResource(value = "contentPublished", blockHandler = "handleBlock", fallback = "handleFallback", blockHandlerClass = ContentBlockHandler.class, fallbackClass = ContentBlockHandler.class)
    public TableDataInfo listPublished(ContentInfo contentInfo)
    {
        startPage();
        contentInfo.setStatus("0");
        List<ContentInfo> list = contentInfoService.selectContentInfoList(contentInfo);
        return getDataTable(list);
    }

    /**
     * Query Recommended Content Info List
     */
    @GetMapping("/recommended")
    @SentinelResource(value = "contentRecommended", blockHandler = "handleBlock", fallback = "handleFallback", blockHandlerClass = ContentBlockHandler.class, fallbackClass = ContentBlockHandler.class)
    public AjaxResult listRecommended()
    {
        List<ContentInfo> list = contentInfoService.selectContentInfoListRecommended();
        return success(list);
    }

    /**
     * Query Top Content Info List
     */
    @GetMapping("/top")
    public AjaxResult listTop()
    {
        List<ContentInfo> list = contentInfoService.selectContentInfoListTop();
        return success(list);
    }

    /**
     * Query Hot Content Info List
     */
    @GetMapping("/hot")
    public AjaxResult listHot(@RequestParam(defaultValue = "10") Integer limit)
    {
        List<ContentInfo> list = contentInfoService.selectContentInfoListHot(limit);
        return success(list);
    }

    /**
     * Query Latest Content Info List
     */
    @GetMapping("/latest")
    @SentinelResource(value = "contentLatest", blockHandler = "handleBlock", fallback = "handleFallback", blockHandlerClass = ContentBlockHandler.class, fallbackClass = ContentBlockHandler.class)
    public AjaxResult listLatest(@RequestParam(defaultValue = "10") Integer limit)
    {
        List<ContentInfo> list = contentInfoService.selectContentInfoListLatest(limit);
        return success(list);
    }

    @GetMapping("/personalized")
    public AjaxResult listPersonalized(@RequestParam(defaultValue = "10") Integer limit)
    {
        Long userId = null;
        try
        {
            userId = SecurityUtils.getUserId();
        }
        catch (Exception ignored) {}
        List<ContentInfo> list = contentInfoService.selectContentInfoPersonalized(userId, limit);
        return success(list);
    }

    @GetMapping("/ranking")
    public AjaxResult ranking(@RequestParam(required = false) Long categoryId,
                              @RequestParam(defaultValue = "all") String period,
                              @RequestParam(defaultValue = "20") Integer limit)
    {
        List<ContentInfo> list = contentInfoMapper.selectContentRanking(categoryId, period, limit);
        return success(list);
    }

    @GetMapping("/tags")
    public AjaxResult hotTags()
    {
        List<Map<String, Object>> tags = contentInfoMapper.selectHotTags();
        return success(tags);
    }

    @GetMapping("/user-content/{userId}")
    public TableDataInfo userContent(@PathVariable Long userId)
    {
        startPage();
        ContentInfo query = new ContentInfo();
        String userName = contentUserMapper.selectContentUserById(userId) != null
            ? contentUserMapper.selectContentUserById(userId).getUserName() : null;
        query.setCreateBy(userName);
        query.setStatus("0");
        List<ContentInfo> list = contentInfoService.selectContentInfoList(query);
        return getDataTable(list);
    }

    @GetMapping("/related/{contentId}")
    public AjaxResult related(@PathVariable("contentId") Long contentId,
                              @RequestParam(defaultValue = "6") Integer limit)
    {
        ContentInfo info = contentInfoService.selectContentInfoByContentId(contentId);
        Long categoryId = info != null ? info.getCategoryId() : null;
        List<ContentInfo> list = contentInfoMapper.selectRelatedContent(contentId, categoryId, limit);
        return success(list);
    }

    @PostMapping("/share/{contentId}")
    public AjaxResult share(@PathVariable("contentId") Long contentId)
    {
        contentInfoMapper.updateContentShareCount(contentId);
        return success();
    }

    @GetMapping("/search")
    @SentinelResource(value = "contentSearch", blockHandler = "handleBlockSearch", fallback = "handleFallbackSearch", blockHandlerClass = ContentBlockHandler.class, fallbackClass = ContentBlockHandler.class)
    public TableDataInfo search(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Long categoryId,
                                @RequestParam(required = false) String contentType,
                                @RequestParam(required = false) String sortBy)
    {
        startPage();
        ContentInfo contentInfo = new ContentInfo();
        contentInfo.setTitle(keyword);
        contentInfo.setCategoryId(categoryId);
        contentInfo.setContentType(contentType);
        contentInfo.setSortBy(sortBy);
        List<ContentInfo> list = contentInfoService.searchContentInfo(contentInfo);
        return getDataTable(list);
    }

    /**
     * Export Content Info List
     */
    @RequiresPermissions("content:info:export")
    @Log(title = "Content Info", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContentInfo contentInfo)
    {
        List<ContentInfo> list = contentInfoService.selectContentInfoList(contentInfo);
        ExcelUtil<ContentInfo> util = new ExcelUtil<ContentInfo>(ContentInfo.class);
        util.exportExcel(response, list, "Content Info Data");
    }

    /**
     * Get Content Info Details
     */
    @RequiresPermissions("content:info:query")
    @GetMapping(value = "/detail/{contentId}")
    public AjaxResult getInfo(@PathVariable("contentId") Long contentId)
    {
        return success(contentInfoService.selectContentInfoByContentId(contentId));
    }

    /**
     * View Content (increment view count)
     */
    @GetMapping("/view/{contentId}")
    @SentinelResource(value = "contentDetail", blockHandler = "handleBlock", fallback = "handleFallback", blockHandlerClass = ContentBlockHandler.class, fallbackClass = ContentBlockHandler.class)
    public AjaxResult view(@PathVariable("contentId") Long contentId)
    {
        try
        {
            contentInfoService.updateContentViewCount(contentId);
        }
        catch (Exception e)
        {
        }
        return success(contentInfoService.selectContentInfoByContentId(contentId));
    }

    /**
     * Like Content
     */
    @Log(title = "Content Info", businessType = BusinessType.UPDATE)
    @PostMapping("/like/{contentId}")
    public AjaxResult like(@PathVariable("contentId") Long contentId)
    {
        Long userId = SecurityUtils.getUserId();
        com.ruoyi.content.domain.ContentLike existing = contentLikeMapper.selectByUserAndContent(userId, contentId);
        if (existing != null)
        {
            contentLikeMapper.deleteByUserAndContent(userId, contentId);
            contentInfoService.updateContentLikeCount(contentId, -1);
            return success(false);
        }
        else
        {
            com.ruoyi.content.domain.ContentLike like = new com.ruoyi.content.domain.ContentLike();
            like.setContentId(contentId);
            like.setUserId(userId);
            contentLikeMapper.insertContentLike(like);
            contentInfoService.updateContentLikeCount(contentId, 1);
            return success(true);
        }
    }

    @GetMapping("/like/check/{contentId}")
    public AjaxResult checkLike(@PathVariable("contentId") Long contentId)
    {
        try
        {
            Long userId = SecurityUtils.getUserId();
            com.ruoyi.content.domain.ContentLike existing = contentLikeMapper.selectByUserAndContent(userId, contentId);
            return success(existing != null);
        }
        catch (Exception e)
        {
            return success(false);
        }
    }

    /**
     * Add Content Info
     */
    @RequiresPermissions("content:info:add")
    @Log(title = "Content Info", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContentInfo contentInfo)
    {
        return toAjax(contentInfoService.insertContentInfo(contentInfo));
    }

    /**
     * Update Content Info
     */
    @RequiresPermissions("content:info:edit")
    @Log(title = "Content Info", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContentInfo contentInfo)
    {
        return toAjax(contentInfoService.updateContentInfo(contentInfo));
    }

    /**
     * Delete Content Info
     */
    @RequiresPermissions("content:info:remove")
    @Log(title = "Content Info", businessType = BusinessType.DELETE)
	@DeleteMapping("/{contentIds}")
    public AjaxResult remove(@PathVariable Long[] contentIds)
    {
        return toAjax(contentInfoService.deleteContentInfoByContentIds(contentIds));
    }

    @PostMapping("/publish")
    public AjaxResult publish(@RequestBody ContentInfo contentInfo)
    {
        Long userId = SecurityUtils.getUserId();
        String userName = SecurityUtils.getUsername();
        contentInfo.setCreateBy(userName);
        contentInfo.setAuthor(userName);
        if (contentInfo.getStatus() == null || contentInfo.getStatus().isEmpty())
        {
            contentInfo.setStatus("1");
        }
        if ("0".equals(contentInfo.getStatus()))
        {
            contentInfo.setPublishTime(new java.util.Date());
        }
        contentInfo.setViewCount(0L);
        contentInfo.setLikeCount(0L);
        contentInfo.setCommentCount(0L);
        contentInfo.setShareCount(0L);
        contentInfo.setIsRecommended("0");
        contentInfo.setIsTop("0");
        return toAjax(contentInfoService.insertContentInfo(contentInfo));
    }

    @GetMapping("/my")
    public TableDataInfo myContent(ContentInfo contentInfo)
    {
        startPage();
        String userName = SecurityUtils.getUsername();
        contentInfo.setAuthor(userName);
        List<ContentInfo> list = contentInfoService.selectContentInfoList(contentInfo);
        return getDataTable(list);
    }

    @PutMapping("/user/update")
    public AjaxResult userUpdate(@RequestBody ContentInfo contentInfo)
    {
        String userName = SecurityUtils.getUsername();
        ContentInfo existing = contentInfoService.selectContentInfoByContentId(contentInfo.getContentId());
        if (existing == null || !userName.equals(existing.getAuthor()))
        {
            return error("无权修改此内容");
        }
        return toAjax(contentInfoService.updateContentInfo(contentInfo));
    }

    @DeleteMapping("/user/{contentId}")
    public AjaxResult userRemove(@PathVariable Long contentId)
    {
        String userName = SecurityUtils.getUsername();
        ContentInfo existing = contentInfoService.selectContentInfoByContentId(contentId);
        if (existing == null || !userName.equals(existing.getAuthor()))
        {
            return error("无权删除此内容");
        }
        return toAjax(contentInfoService.deleteContentInfoByContentIds(new Long[]{contentId}));
    }

    @RequiresPermissions("content:info:edit")
    @Log(title = "内容审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{contentId}")
    public AjaxResult audit(@PathVariable Long contentId, @RequestParam String status, @RequestParam(required = false) String auditRemark)
    {
        ContentInfo contentInfo = new ContentInfo();
        contentInfo.setContentId(contentId);
        contentInfo.setStatus(status);
        if (auditRemark != null && !auditRemark.isEmpty())
        {
            contentInfo.setRemark(auditRemark);
        }
        if ("0".equals(status))
        {
            contentInfo.setPublishTime(new java.util.Date());
        }
        int result = contentInfoService.updateContentInfo(contentInfo);
        if (result > 0 && "0".equals(status))
        {
            cacheWarmer.warmContentDetail(contentId);
            cacheWarmer.warmContentLists();
        }
        return toAjax(result);
    }

    @RequiresPermissions("content:info:list")
    @GetMapping("/stats")
    public AjaxResult stats()
    {
        Map<String, Object> statsMap = contentInfoMapper.selectContentStats();
        if (statsMap == null)
        {
            statsMap = new HashMap<>();
        }
        List<Map<String, Object>> typeStats = contentInfoMapper.selectContentTypeStats();
        List<Map<String, Object>> recentStats = contentInfoMapper.selectRecentContentStats();
        statsMap.put("typeStats", typeStats);
        statsMap.put("recentStats", recentStats);
        return success(statsMap);
    }

    @RequiresPermissions("content:info:edit")
    @GetMapping("/pending")
    public TableDataInfo pendingList(ContentInfo contentInfo)
    {
        startPage();
        contentInfo.setStatus("1");
        List<ContentInfo> list = contentInfoService.selectContentInfoList(contentInfo);
        return getDataTable(list);
    }
}
