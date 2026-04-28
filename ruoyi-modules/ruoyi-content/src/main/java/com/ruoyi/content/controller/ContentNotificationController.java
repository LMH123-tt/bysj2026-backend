package com.ruoyi.content.controller;

import java.util.List;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.content.domain.ContentNotification;
import com.ruoyi.content.mapper.ContentNotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ruoyi.common.core.web.domain.AjaxResult.success;

@RestController
@RequestMapping("/content/notification")
public class ContentNotificationController
{
    @Autowired
    private ContentNotificationMapper notificationMapper;

    @GetMapping("/list")
    public AjaxResult list()
    {
        Long userId = SecurityUtils.getUserId();
        List<ContentNotification> list = notificationMapper.selectByUserId(userId);
        return success(list);
    }

    @GetMapping("/unread")
    public AjaxResult unreadCount()
    {
        Long userId = SecurityUtils.getUserId();
        int count = notificationMapper.selectUnreadCount(userId);
        return success(count);
    }

    @PutMapping("/read/{id}")
    public AjaxResult markRead(@PathVariable Long id)
    {
        Long userId = SecurityUtils.getUserId();
        notificationMapper.markAsRead(id, userId);
        return success();
    }

    @PutMapping("/readAll")
    public AjaxResult markAllRead()
    {
        Long userId = SecurityUtils.getUserId();
        notificationMapper.markAllAsRead(userId);
        return success();
    }

    @DeleteMapping("/{id}")
    public AjaxResult delete(@PathVariable Long id)
    {
        Long userId = SecurityUtils.getUserId();
        notificationMapper.deleteById(id, userId);
        return success();
    }
}
