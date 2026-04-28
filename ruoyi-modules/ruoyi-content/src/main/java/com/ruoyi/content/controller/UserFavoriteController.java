package com.ruoyi.content.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.ruoyi.content.domain.UserFavorite;
import com.ruoyi.content.service.IUserFavoriteService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * C-End User Favorite Controller
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
@RestController
@RequestMapping("/content/favorite")
public class UserFavoriteController extends BaseController
{
    @Autowired
    private IUserFavoriteService userFavoriteService;

    /**
     * Query User Favorite List
     */
    @RequiresPermissions("content:favorite:list")
    @GetMapping("/list")
    public TableDataInfo list(UserFavorite userFavorite)
    {
        startPage();
        List<UserFavorite> list = userFavoriteService.selectUserFavoriteList(userFavorite);
        return getDataTable(list);
    }

    @GetMapping("/my")
    public TableDataInfo myFavorites()
    {
        startPage();
        UserFavorite userFavorite = new UserFavorite();
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                userFavorite.setUserId(userId);
            }
        } catch (Exception ignored) {}
        List<UserFavorite> list = userFavoriteService.selectUserFavoriteList(userFavorite);
        return getDataTable(list);
    }

    /**
     * Query User Favorite List by User ID
     */
    @GetMapping("/user/{userId}")
    public AjaxResult listByUserId(@PathVariable("userId") Long userId)
    {
        List<UserFavorite> list = userFavoriteService.selectUserFavoriteListByUserId(userId);
        return success(list);
    }

    /**
     * Check if user has favorited content
     */
    @GetMapping("/check")
    public AjaxResult checkFavorite(@RequestParam Long contentId)
    {
        try
        {
            Long userId = SecurityUtils.getUserId();
            if (userId != null)
            {
                boolean exists = userFavoriteService.selectUserFavoriteExists(userId, contentId);
                return success(exists);
            }
        }
        catch (Exception ignored) {}
        return success(false);
    }

    /**
     * Export User Favorite List
     */
    @RequiresPermissions("content:favorite:export")
    @Log(title = "User Favorite", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserFavorite userFavorite)
    {
        List<UserFavorite> list = userFavoriteService.selectUserFavoriteList(userFavorite);
        ExcelUtil<UserFavorite> util = new ExcelUtil<UserFavorite>(UserFavorite.class);
        util.exportExcel(response, list, "User Favorite Data");
    }

    /**
     * Get User Favorite Details
     */
    @RequiresPermissions("content:favorite:query")
    @GetMapping(value = "/{favoriteId}")
    public AjaxResult getInfo(@PathVariable("favoriteId") Long favoriteId)
    {
        return success(userFavoriteService.selectUserFavoriteByFavoriteId(favoriteId));
    }

    /**
     * Add User Favorite
     */
    @PostMapping
    public AjaxResult add(@RequestBody UserFavorite userFavorite)
    {
        try {
            Long userId = SecurityUtils.getUserId();
            if (userId != null) {
                userFavorite.setUserId(userId);
            }
        } catch (Exception ignored) {}
        return toAjax(userFavoriteService.insertUserFavorite(userFavorite));
    }

    @DeleteMapping("/cancel")
    public AjaxResult cancelByContentId(@RequestParam Long contentId)
    {
        try
        {
            Long userId = SecurityUtils.getUserId();
            if (userId != null)
            {
                return toAjax(userFavoriteService.deleteUserFavoriteByUserIdAndContentId(userId, contentId));
            }
        }
        catch (Exception ignored) {}
        return error("请先登录");
    }

    /**
     * Delete User Favorite
     */
    @RequiresPermissions("content:favorite:remove")
    @Log(title = "User Favorite", businessType = BusinessType.DELETE)
	@DeleteMapping("/{favoriteIds}")
    public AjaxResult remove(@PathVariable Long[] favoriteIds)
    {
        return toAjax(userFavoriteService.deleteUserFavoriteByFavoriteIds(favoriteIds));
    }

    /**
     * Delete User Favorite by User ID and Content ID
     */
    @Log(title = "User Favorite", businessType = BusinessType.DELETE)
    @DeleteMapping("/user/{userId}/content/{contentId}")
    public AjaxResult removeByUserIdAndContentId(@PathVariable("userId") Long userId, @PathVariable("contentId") Long contentId)
    {
        return toAjax(userFavoriteService.deleteUserFavoriteByUserIdAndContentId(userId, contentId));
    }
}
