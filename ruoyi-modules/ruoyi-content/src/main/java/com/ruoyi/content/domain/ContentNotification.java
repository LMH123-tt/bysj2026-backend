package com.ruoyi.content.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class ContentNotification
{
    private Long id;
    private Long userId;
    private Long fromUserId;
    private String type;
    private String title;
    private String content;
    private Long relatedId;
    private String isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String fromUserName;
    private String fromUserAvatar;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getFromUserId() { return fromUserId; }
    public void setFromUserId(Long fromUserId) { this.fromUserId = fromUserId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Long getRelatedId() { return relatedId; }
    public void setRelatedId(Long relatedId) { this.relatedId = relatedId; }
    public String getIsRead() { return isRead; }
    public void setIsRead(String isRead) { this.isRead = isRead; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getFromUserName() { return fromUserName; }
    public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }
    public String getFromUserAvatar() { return fromUserAvatar; }
    public void setFromUserAvatar(String fromUserAvatar) { this.fromUserAvatar = fromUserAvatar; }
}
