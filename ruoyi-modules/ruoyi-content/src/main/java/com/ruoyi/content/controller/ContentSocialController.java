package com.ruoyi.content.controller;

import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.content.domain.UserFriend;
import com.ruoyi.content.domain.UserMessage;
import com.ruoyi.content.domain.ContentNotification;
import com.ruoyi.content.mapper.UserFriendMapper;
import com.ruoyi.content.mapper.UserMessageMapper;
import com.ruoyi.content.mapper.ContentNotificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ruoyi.common.core.web.domain.AjaxResult.success;

@RestController
@RequestMapping("/content/social")
public class ContentSocialController
{
    @Autowired
    private UserFriendMapper userFriendMapper;

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Autowired
    private ContentNotificationMapper notificationMapper;

    @GetMapping("/friend/list")
    public AjaxResult friendList(@RequestParam(required = false) String status)
    {
        Long userId = SecurityUtils.getUserId();
        List<UserFriend> list = userFriendMapper.selectFriendList(userId, status);
        return success(list);
    }

    @GetMapping("/friend/requests")
    public AjaxResult friendRequests()
    {
        Long userId = SecurityUtils.getUserId();
        List<UserFriend> list = userFriendMapper.selectReceivedRequests(userId);
        return success(list);
    }

    @PostMapping("/friend/add")
    public AjaxResult addFriend(@RequestBody UserFriend userFriend)
    {
        Long userId = SecurityUtils.getUserId();
        Long friendId = userFriend.getFriendId();
        if (userId.equals(friendId))
        {
            return AjaxResult.error("不能添加自己为好友");
        }
        UserFriend existing = userFriendMapper.selectFriendById(userId, friendId);
        if (existing != null)
        {
            return AjaxResult.error("已发送过好友请求或已是好友");
        }
        UserFriend friend = new UserFriend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setStatus("0");
        userFriendMapper.insertFriend(friend);

        ContentNotification notification = new ContentNotification();
        notification.setUserId(friendId);
        notification.setFromUserId(userId);
        notification.setType("friend_request");
        notification.setTitle("好友请求");
        notification.setContent("请求添加你为好友");
        notification.setRelatedId(friendId);
        notificationMapper.insertNotification(notification);

        return success();
    }

    @PutMapping("/friend/accept/{friendId}")
    public AjaxResult acceptFriend(@PathVariable Long friendId)
    {
        Long userId = SecurityUtils.getUserId();
        UserFriend friend = userFriendMapper.selectFriendById(friendId, userId);
        if (friend == null)
        {
            return AjaxResult.error("好友请求不存在");
        }
        if ("1".equals(friend.getStatus()))
        {
            return AjaxResult.error("已是好友关系");
        }
        friend.setStatus("1");
        userFriendMapper.updateFriendStatus(friend);

        UserFriend reverse = new UserFriend();
        reverse.setUserId(userId);
        reverse.setFriendId(friendId);
        reverse.setStatus("1");
        UserFriend reverseExisting = userFriendMapper.selectFriendById(userId, friendId);
        if (reverseExisting == null)
        {
            userFriendMapper.insertFriend(reverse);
        }
        else
        {
            reverseExisting.setStatus("1");
            userFriendMapper.updateFriendStatus(reverseExisting);
        }

        ContentNotification notification = new ContentNotification();
        notification.setUserId(friendId);
        notification.setFromUserId(userId);
        notification.setType("friend_accept");
        notification.setTitle("好友通过");
        notification.setContent("已接受你的好友请求");
        notificationMapper.insertNotification(notification);

        return success();
    }

    @DeleteMapping("/friend/{friendId}")
    public AjaxResult deleteFriend(@PathVariable Long friendId)
    {
        Long userId = SecurityUtils.getUserId();
        userFriendMapper.deleteFriend(userId, friendId);
        userFriendMapper.deleteFriend(friendId, userId);
        return success();
    }

    @GetMapping("/friend/unread")
    public AjaxResult unreadRequestCount()
    {
        Long userId = SecurityUtils.getUserId();
        int count = userFriendMapper.selectUnreadRequestCount(userId);
        return success(count);
    }

    @GetMapping("/message/list/{friendId}")
    public AjaxResult messageList(@PathVariable Long friendId)
    {
        Long userId = SecurityUtils.getUserId();
        List<UserMessage> list = userMessageMapper.selectChatList(userId, friendId);
        userMessageMapper.markAsRead(userId, friendId);
        return success(list);
    }

    @GetMapping("/message/recent")
    public AjaxResult recentChats()
    {
        Long userId = SecurityUtils.getUserId();
        List<Map<String, Object>> list = userMessageMapper.selectRecentChats(userId);
        return success(list);
    }

    @PostMapping("/message/send")
    public AjaxResult sendMessage(@RequestBody UserMessage userMessage)
    {
        Long userId = SecurityUtils.getUserId();
        userMessage.setSenderId(userId);
        userMessageMapper.insertMessage(userMessage);
        return success();
    }

    @GetMapping("/message/unread")
    public AjaxResult unreadMessageCount()
    {
        Long userId = SecurityUtils.getUserId();
        int count = userMessageMapper.selectUnreadCount(userId);
        return success(count);
    }
}
