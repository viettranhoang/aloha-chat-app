package com.example.appchat_zalo.choose_friends;

import androidx.annotation.StringDef;

import static com.example.appchat_zalo.message.adapter.MessageTypeConfig.IMAGE;
import static com.example.appchat_zalo.message.adapter.MessageTypeConfig.TEXT;

@StringDef({TEXT, IMAGE})
public @interface ChooseTypeConfig {
    String MEETING_GROUP = "meeting group";
    String LEARNING_GROUP = "learning group";
    String DATING_GROUP = "dating group";
}