package com.example.appchat_zalo.comment.model;

import com.example.appchat_zalo.model.Posts;
import com.example.appchat_zalo.model.Users;

public class Comment {
    private Users uses;
    private String content;
    private String date;
    private String time;
    private String type;

    public Comment(Users uses, String content, String date, String time, String type) {
        this.uses = uses;
        this.content = content;
        this.date = date;
        this.time = time;
        this.type = type;
    }

    public Comment() {
    }

    public Users getUses() {
        return uses;
    }

    public void setUses(Users uses) {
        this.uses = uses;
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
                "uses=" + uses +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
