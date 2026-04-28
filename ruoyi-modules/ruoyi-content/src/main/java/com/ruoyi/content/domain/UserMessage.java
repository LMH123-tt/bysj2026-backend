package com.ruoyi.content.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.web.domain.BaseEntity;

public class UserMessage extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String isRead;
    private String senderName;
    private String senderAvatar;
    private String receiverName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getIsRead() { return isRead; }
    public void setIsRead(String isRead) { this.isRead = isRead; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getSenderAvatar() { return senderAvatar; }
    public void setSenderAvatar(String senderAvatar) { this.senderAvatar = senderAvatar; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
}
