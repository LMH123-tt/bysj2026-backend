package com.ruoyi.content.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.content.domain.UserFriend;
@Mapper
public interface UserFriendMapper
{
    public List<UserFriend> selectFriendList(@Param("userId") Long userId, @Param("status") String status);
    public List<UserFriend> selectReceivedRequests(@Param("userId") Long userId);
    public UserFriend selectFriendById(@Param("userId") Long userId, @Param("friendId") Long friendId);
    public int insertFriend(UserFriend userFriend);
    public int updateFriendStatus(UserFriend userFriend);
    public int deleteFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);
    public int selectUnreadRequestCount(@Param("userId") Long userId);
}
