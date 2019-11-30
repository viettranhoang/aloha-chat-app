package com.example.appchat_zalo;

import androidx.annotation.StringDef;

import static com.example.appchat_zalo.message.adapter.MessageTypeConfig.IMAGE;
import static com.example.appchat_zalo.message.adapter.MessageTypeConfig.TEXT;

@StringDef({TEXT, IMAGE})
public @interface CheckLikeTypeConfig {
    String LIKE = "true";
    String DISLIKE = "false";
}