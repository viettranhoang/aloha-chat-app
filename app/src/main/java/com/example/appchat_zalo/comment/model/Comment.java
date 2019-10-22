package com.example.appchat_zalo.comment.model;

import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;

public class Comment {
    private String userId;
    private String commentId;
    private String nameUser;

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    private String avatarUser;
    private String content;
    private String date;
    private String time;
    private String type;

    public Comment() {
    }

    public Comment(String userId, String commentId, String nameUser, String avatarUser, String content, String date, String time, String type) {
        this.userId = userId;
        this.commentId = commentId;
        this.nameUser = nameUser;
        this.avatarUser = avatarUser;
        this.content = content;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "userId='" + userId + '\'' +
                ", commentId='" + commentId + '\'' +
                ", nameUser='" + nameUser + '\'' +
                ", avatarUser='" + avatarUser + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
