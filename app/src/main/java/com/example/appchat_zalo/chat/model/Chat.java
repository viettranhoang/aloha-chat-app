package com.example.appchat_zalo.chat.model;

import com.example.appchat_zalo.model.Message;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;

public class Chat {

    Users users;
    Groups groups;
    Message lastMessage;

    public Chat() {
    }

    public Chat(Users users, Groups groups, Message lastMessage) {
        this.users = users;
        this.groups = groups;
        this.lastMessage = lastMessage;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Groups getGroups() {
        return groups;
    }

    public void setGroups(Groups groups) {
        this.groups = groups;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "users=" + users +
                ", groups=" + groups +
                ", lastMessage=" + lastMessage +
                '}';
    }
}
