package com.ruoyi.content.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.content.domain.UserMessage;
@Mapper
public interface UserMessageMapper
{
    public List<UserMessage> selectChatList(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    public List<Map<String, Object>> selectRecentChats(@Param("userId") Long userId);
    public int insertMessage(UserMessage userMessage);
    public int markAsRead(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
    public int selectUnreadCount(@Param("receiverId") Long receiverId);
}
