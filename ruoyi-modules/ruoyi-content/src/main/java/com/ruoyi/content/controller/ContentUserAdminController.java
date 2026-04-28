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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.content.domain.ContentUser;
import com.ruoyi.content.service.IContentUserService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

@RestController
@RequestMapping("/content/user")
public class ContentUserAdminController extends BaseController
{
    @Autowired
    private IContentUserService contentUserService;

    @RequiresPermissions("content:user:list")
    @GetMapping("/list")
    public TableDataInfo list(ContentUser contentUser)
    {
        startPage();
        List<ContentUser> list = contentUserService.selectContentUserList(contentUser);
        return getDataTable(list);
    }

    @RequiresPermissions("content:user:query")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        return success(contentUserService.selectContentUserById(userId));
    }

    @RequiresPermissions("content:user:edit")
    @Log(title = "C端用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContentUser contentUser)
    {
        return toAjax(contentUserService.updateContentUser(contentUser));
    }

    @RequiresPermissions("content:user:remove")
    @Log(title = "C端用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(contentUserService.deleteContentUserByIds(userIds));
    }

    @RequiresPermissions("content:user:edit")
    @Log(title = "C端用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestParam Long userId, @RequestParam String password)
    {
        ContentUser user = new ContentUser();
        user.setUserId(userId);
        user.setPassword(SecurityUtils.encryptPassword(password));
        return toAjax(contentUserService.updateContentUser(user));
    }

    @RequiresPermissions("content:user:export")
    @Log(title = "C端用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContentUser contentUser)
    {
        List<ContentUser> list = contentUserService.selectContentUserList(contentUser);
        ExcelUtil<ContentUser> util = new ExcelUtil<ContentUser>(ContentUser.class);
        util.exportExcel(response, list, "C端用户数据");
    }
}
