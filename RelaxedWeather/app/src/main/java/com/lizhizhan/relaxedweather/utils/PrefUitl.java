package com.lizhizhan.relaxedweather.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lizhizhan on 2016/9/4.
 */

public class PrefUitl {
    public static boolean getBoolean(Context cxt, String key, Boolean defValue) {
        SharedPreferences sp = cxt.getSharedPreferences("config", cxt.MODE_PRIVATE);

        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(Context cxt, String key, Boolean defValue) {
        SharedPreferences sp = cxt.getSharedPreferences("config", cxt.MODE_PRIVATE);
        sp.edit().putBoolean(key, defValue).commit();
    }

    public static String getString(Context cxt, String key, String defValue) {
        SharedPreferences sp = cxt.getSharedPreferences("config", cxt.MODE_PRIVATE);

        return sp.getString(key, defValue);
    }

    public static void setString(Context cxt, String key, String defValue) {
        SharedPreferences sp = cxt.getSharedPreferences("config", cxt.MODE_PRIVATE);
        sp.edit().putString(key, defValue).commit();
    }

    public static int getInt(Context cxt, String key, int defValue) {
        SharedPreferences sp = cxt.getSharedPreferences("config", cxt.MODE_PRIVATE);

        return sp.getInt(key, defValue);
    }

    public static void setint(Context cxt, String key, int Value) {
        SharedPreferences sp = cxt.getSharedPreferences("config", cxt.MODE_PRIVATE);
        sp.edit().putInt(key, Value).commit();
    }

}
