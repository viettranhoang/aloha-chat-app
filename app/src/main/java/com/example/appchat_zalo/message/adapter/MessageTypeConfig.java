package com.example.appchat_zalo.message.adapter;

import androidx.annotation.StringDef;

import static com.example.appchat_zalo.message.adapter.MessageTypeConfig.IMAGE;
import static com.example.appchat_zalo.message.adapter.MessageTypeConfig.TEXT;

@StringDef({TEXT, IMAGE})
public @interface MessageTypeConfig {
    String TEXT = "text";
    String IMAGE = "image";
}