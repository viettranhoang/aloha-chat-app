package com.example.appchat_zalo.Message.adapter;

import androidx.annotation.StringDef;

import static com.example.appchat_zalo.Message.adapter.MessageTypeConfig.IMAGE;
import static com.example.appchat_zalo.Message.adapter.MessageTypeConfig.TEXT;

@StringDef({TEXT, IMAGE})
public @interface MessageTypeConfig {
    String TEXT = "text";
    String IMAGE = "image";
}