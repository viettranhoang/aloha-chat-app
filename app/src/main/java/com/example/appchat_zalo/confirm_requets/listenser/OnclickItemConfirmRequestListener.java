package com.example.appchat_zalo.confirm_requets.listenser;

import com.example.appchat_zalo.model.Users;

public interface  OnclickItemConfirmRequestListener {
    void onclickAccpet(Users users);
    void onclickCancel(Users users);
    void onclickRecive(Users users);
}
