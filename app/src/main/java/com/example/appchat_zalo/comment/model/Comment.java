package com.example.appchat_zalo.comment.model;

import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;

public class Comment {
    private String UserId;
    private String CommentId;
    private String content;
    private String time;
    private String postId;
    private String type;

    public Comment(String userId, String commentId, String content, String time, String postId, String type) {
        UserId = userId;
        CommentId = commentId;
        this.content = content;
        this.time = time;
        this.postId = postId;
        this.type = type;
    }

    public Comment() {
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
                "UserId='" + UserId + '\'' +
                ", CommentId='" + CommentId + '\'' +
                ", content='" + content + '\'' +
                ", time='" + time + '\'' +
                ", postId='" + postId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
