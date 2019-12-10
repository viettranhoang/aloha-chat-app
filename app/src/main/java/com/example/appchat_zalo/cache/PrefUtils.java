package com.example.appchat_zalo.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static PrefUtils prefUtils = null;

    private static final String TAG = PrefUtils.class.getSimpleName();
    private static final String PREF_NAME = "CHAT_PREF";

    private SharedPreferences prefs;

    PrefUtils(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PrefUtils getIntance(Context context) {
        if (prefUtils == null) {
            prefUtils = new PrefUtils(context);
        }
        return prefUtils;
    }

    public void set(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public String get(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public void set(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    public int get(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public void set(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    public boolean get(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public void set(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    public long get(String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public void clearKey(String key) {
        prefs.edit().remove(key).apply();
    }

    public void clearAllKey() {
        prefs.edit().clear().apply();
    }

    public String getCurrentUid() {
        return get(PREF_KEY.CURRENT_USER_ID, null);
    }

    public String getCurrentUAvatar() {
        return get(PREF_KEY.CURRENT_USER_AVATAR, null);
    }

    public void setCurrentUid(String uid){
        set(PrefUtils.PREF_KEY.CURRENT_USER_ID, uid);
    }

    public void setCurrentUAvatar(String avatar){
        set(PREF_KEY.CURRENT_USER_AVATAR, avatar);
    }

    public interface PREF_KEY {
        String CURRENT_USER_ID = "CURRENT_USER_ID";
        String CURRENT_USER_AVATAR = "CURRENT_USER_AVATAR";

    }
}
