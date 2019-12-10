package com.example.appchat_zalo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private String id;
    private String name;
    private String status;
    private String avatar;
    private String cover;
    private String news;
    private String posts;
    private long online;

    public Users(String id, String name, String status, String avatar, String cover, String news, String posts, long online) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.avatar = avatar;
        this.cover = cover;
        this.news = news;
        this.posts = posts;
        this.online = online;
    }

    public Users() {
    }

    protected Users(Parcel in) {
        id = in.readString();
        name = in.readString();
        status = in.readString();
        avatar = in.readString();
        cover = in.readString();
        news = in.readString();
        posts = in.readString();
        online = in.readLong();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getPosts() {
        return posts;
    }

    public void setPosts(String posts) {
        this.posts = posts;
    }

    public long getOnline() {
        return online;
    }

    public void setOnline(long online) {
        this.online = online;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", news='" + news + '\'' +
                ", posts='" + posts + '\'' +
                ", online=" + online +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(status);
        parcel.writeString(avatar);
        parcel.writeString(cover);
        parcel.writeString(news);
        parcel.writeString(posts);
        parcel.writeLong(online);
    }
}
