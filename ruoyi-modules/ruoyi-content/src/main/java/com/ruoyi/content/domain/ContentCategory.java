package com.ruoyi.content.domain;

import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * C-End Content Category Object
 * 
 * @author ruoyi
 * @date 2026-04-09
 */
public class ContentCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Category ID */
    private Long categoryId;

    /** Category Name */
    @Excel(name = "Category Name")
    private String categoryName;

    /** Parent Category ID */
    @Excel(name = "Parent Category ID")
    private Long parentId;

    /** Display Order */
    @Excel(name = "Display Order")
    private Integer orderNum;

    /** Category Status (0-normal 1-disabled) */
    @Excel(name = "Category Status", readConverterExp = "0=normal,1=disabled")
    private String status;

    /** Category Icon */
    @Excel(name = "Category Icon")
    private String icon;

    /** Category Description */
    @Excel(name = "Category Description")
    private String description;

    /** Content Count */
    private Integer contentCount;

    public void setCategoryId(Long categoryId) 
    {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() 
    {
        return categoryId;
    }

    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }

    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setContentCount(Integer contentCount) 
    {
        this.contentCount = contentCount;
    }

    public Integer getContentCount() 
    {
        return contentCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("categoryId", getCategoryId())
            .append("categoryName", getCategoryName())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("status", getStatus())
            .append("icon", getIcon())
            .append("description", getDescription())
            .append("contentCount", getContentCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
