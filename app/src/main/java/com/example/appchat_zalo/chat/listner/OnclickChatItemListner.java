package com.example.appchat_zalo.chat.listner;

import com.example.appchat_zalo.chat.model.Chat;
import com.example.appchat_zalo.model.Groups;
import com.example.appchat_zalo.model.Users;

public interface OnclickChatItemListner {
    void onClickUserChatItem(Users user);

    void onClickGroupItem(Groups group);

}
