package com.example.appchat_zalo.my_profile;

import androidx.annotation.StringDef;

import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.FRIEND;
import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.NOT;
import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.RECEIVE;
import static com.example.appchat_zalo.my_profile.UserRelationshipConfig.SENT;

@StringDef({FRIEND, SENT, RECEIVE, NOT})
public @interface UserRelationshipConfig {
    String FRIEND = "friend";
    String SENT = "sent";
    String RECEIVE = "receive";
    String NOT = "not";
}