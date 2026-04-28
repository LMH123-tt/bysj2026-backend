package com.ruoyi.content.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;

@Component
public class ContentFallbackFactory implements FallbackFactory<Object>
{
    private static final Logger log = LoggerFactory.getLogger(ContentFallbackFactory.class);

    @Override
    public Object create(Throwable cause)
    {
        log.error("内容服务调用失败，触发降级: {}", cause.getMessage());
        return new ContentFallback(cause);
    }

    public static class ContentFallback
    {
        private final Throwable cause;

        public ContentFallback(Throwable cause)
        {
            this.cause = cause;
        }

        public AjaxResult listPublished()
        {
            return AjaxResult.success(new ArrayList<>());
        }

        public AjaxResult listRecommended()
        {
            return AjaxResult.success(new ArrayList<>());
        }

        public AjaxResult listTop()
        {
            return AjaxResult.success(new ArrayList<>());
        }

        public AjaxResult listHot(Integer limit)
        {
            return AjaxResult.success(new ArrayList<>());
        }

        public AjaxResult listLatest(Integer limit)
        {
            return AjaxResult.success(new ArrayList<>());
        }

        public TableDataInfo search(String keyword, Long categoryId, String contentType)
        {
            TableDataInfo info = new TableDataInfo();
            info.setRows(new ArrayList<>());
            info.setTotal(0);
            return info;
        }

        public AjaxResult view(Long contentId)
        {
            Map<String, Object> data = new HashMap<>();
            data.put("contentId", contentId);
            data.put("title", "服务暂时不可用");
            data.put("summary", "内容服务正在维护中，请稍后再试");
            return AjaxResult.success(data);
        }

        public AjaxResult listCategoryEnabled()
        {
            return AjaxResult.success(new ArrayList<>());
        }
    }
}
