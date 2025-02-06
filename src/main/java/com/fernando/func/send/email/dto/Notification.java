package com.fernando.func.send.email.dto;

public class Notification {
    private Long userId;
    private String targetType;
    private String targetId;
    private String content;
    private String datePost;

    
    public Notification() {
    }

    public Notification(Long userId, String targetType, String targetId, String content, String datePost) {
        this.userId = userId;
        this.targetType = targetType;
        this.targetId = targetId;
        this.content = content;
        this.datePost = datePost;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
