package com.example.appchat_zalo.comment;

import androidx.annotation.StringDef;

import static com.example.appchat_zalo.Message.adapter.MessageTypeConfig.IMAGE;
import static com.example.appchat_zalo.Message.adapter.MessageTypeConfig.TEXT;

@StringDef({TEXT, IMAGE})
public @interface CommentTypeConfig {
    String TEXT = "text";
    String IMAGE = "image";
}