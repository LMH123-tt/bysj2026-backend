package com.ruoyi.content.mapper;

import java.util.List;
import com.ruoyi.content.domain.ContentNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ContentNotificationMapper
{
    public List<ContentNotification> selectByUserId(@Param("userId") Long userId);
    public int selectUnreadCount(@Param("userId") Long userId);
    public int insertNotification(ContentNotification notification);
    public int markAsRead(@Param("id") Long id, @Param("userId") Long userId);
    public int markAllAsRead(@Param("userId") Long userId);
    public int deleteById(@Param("id") Long id, @Param("userId") Long userId);
}
