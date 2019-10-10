package com.example.appchat_zalo.utils;

import android.util.Patterns;

import java.text.SimpleDateFormat;

public class Utils {

    public static String getTime(long minisecondTime) {
        String currentDay = new SimpleDateFormat("dd").format(System.currentTimeMillis());
        String day = new SimpleDateFormat("dd").format(minisecondTime);

        if (currentDay.equals(day)) {
            return new SimpleDateFormat("HH:mm").format(minisecondTime);
        } else
            return new SimpleDateFormat("E").format(minisecondTime);
    }

    public static boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
        return target.length() >= 10 && target.length() < 13 && Patterns.PHONE.matcher(target).matches();
    }

    public static boolean isValidPassword(CharSequence target) {
        return target.length() >= 6;
    }
}


