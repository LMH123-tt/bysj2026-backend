package com.ruoyi.content.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.content.domain.ContentCategory;
import com.ruoyi.content.service.IContentCategoryService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * C-End Content Category Controller
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
@RestController
@RequestMapping("/content/category")
public class ContentCategoryController extends BaseController
{
    @Autowired
    private IContentCategoryService contentCategoryService;

    /**
     * Query Content Category List
     */
    @RequiresPermissions("content:category:list")
    @GetMapping("/list")
    public TableDataInfo list(ContentCategory contentCategory)
    {
        startPage();
        List<ContentCategory> list = contentCategoryService.selectContentCategoryList(contentCategory);
        return getDataTable(list);
    }

    /**
     * Query Enabled Content Category List
     */
    @GetMapping("/enabled")
    public AjaxResult listEnabled()
    {
        List<ContentCategory> list = contentCategoryService.selectContentCategoryListEnabled();
        return success(list);
    }

    /**
     * Export Content Category List
     */
    @RequiresPermissions("content:category:export")
    @Log(title = "Content Category", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ContentCategory contentCategory)
    {
        List<ContentCategory> list = contentCategoryService.selectContentCategoryList(contentCategory);
        ExcelUtil<ContentCategory> util = new ExcelUtil<ContentCategory>(ContentCategory.class);
        util.exportExcel(response, list, "Content Category Data");
    }

    /**
     * Get Content Category Details
     */
    @RequiresPermissions("content:category:query")
    @GetMapping(value = "/{categoryId}")
    public AjaxResult getInfo(@PathVariable("categoryId") Long categoryId)
    {
        return success(contentCategoryService.selectContentCategoryByCategoryId(categoryId));
    }

    /**
     * Add Content Category
     */
    @RequiresPermissions("content:category:add")
    @Log(title = "Content Category", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContentCategory contentCategory)
    {
        return toAjax(contentCategoryService.insertContentCategory(contentCategory));
    }

    /**
     * Update Content Category
     */
    @RequiresPermissions("content:category:edit")
    @Log(title = "Content Category", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContentCategory contentCategory)
    {
        return toAjax(contentCategoryService.updateContentCategory(contentCategory));
    }

    /**
     * Delete Content Category
     */
    @RequiresPermissions("content:category:remove")
    @Log(title = "Content Category", businessType = BusinessType.DELETE)
	@DeleteMapping("/{categoryIds}")
    public AjaxResult remove(@PathVariable Long[] categoryIds)
    {
        return toAjax(contentCategoryService.deleteContentCategoryByCategoryIds(categoryIds));
    }
}
