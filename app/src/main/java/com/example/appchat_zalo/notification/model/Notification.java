package com.example.appchat_zalo.notification.model;

public class Notification {
    private String mUserId;
    private String mText;
    private String mPostId;
    private String mNotifiId;
    public Notification() {
    }

    public Notification(String mUserId, String mText, String mPostId, String mNotifiId) {
        this.mUserId = mUserId;
        this.mText = mText;
        this.mPostId = mPostId;
        this.mNotifiId = mNotifiId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmNotifiId() {
        return mNotifiId;
    }

    public void setmNotifiId(String mNotifiId) {
        this.mNotifiId = mNotifiId;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmPostId() {
        return mPostId;
    }

    public void setmPostId(String mPostId) {
        this.mPostId = mPostId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "mUserId='" + mUserId + '\'' +
                ", mText='" + mText + '\'' +
                ", mPostId='" + mPostId + '\'' +
                ", mNotifiId='" + mNotifiId + '\'' +
                '}';
    }
}
