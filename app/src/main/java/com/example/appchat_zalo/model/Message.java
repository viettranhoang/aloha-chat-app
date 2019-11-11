package com.example.appchat_zalo.model;

public class Message {

    private String message;
    private String from;
    private boolean seen;
    private long time;
    private String type;
    private String from_avatar;

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public Message(String message, String from, boolean seen, long time, String type, String from_avatar) {
        this.message = message;
        this.from = from;
        this.seen = seen;
        this.time = time;
        this.type = type;
        this.from_avatar = from_avatar;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", from='" + from + '\'' +
                ", seen=" + seen +
                ", time=" + time +
                ", type='" + type + '\'' +
                ", from_avatar='" + from_avatar + '\'' +
                '}';
    }
}
