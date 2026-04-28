package com.ruoyi.content.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class UserFriend extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private Long friendId;
    private String status;
    private String friendName;
    private String friendAvatar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getFriendId() { return friendId; }
    public void setFriendId(Long friendId) { this.friendId = friendId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getFriendName() { return friendName; }
    public void setFriendName(String friendName) { this.friendName = friendName; }
    public String getFriendAvatar() { return friendAvatar; }
    public void setFriendAvatar(String friendAvatar) { this.friendAvatar = friendAvatar; }
}
