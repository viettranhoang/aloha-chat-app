package com.example.appchat_zalo.model;

import java.io.Serializable;

public class Posts {
    private String date;
    private String time;
    private String content_posts;
    private String picture;
    private String avatar;
    private String name;
    private String idPost;

    public Posts(String date, String time, String content_posts, String picture, String avatar, String name, String idPost) {
        this.date = date;
        this.time = time;
        this.content_posts = content_posts;
        this.picture = picture;
        this.avatar = avatar;
        this.name = name;
        this.idPost = idPost;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public Posts() {
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

    public String getContent_posts() {
        return content_posts;
    }

    public void setContent_posts(String content_posts) {
        this.content_posts = content_posts;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", content_posts='" + content_posts + '\'' +
                ", picture='" + picture + '\'' +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", idPost='" + idPost + '\'' +
                '}';
    }
}
