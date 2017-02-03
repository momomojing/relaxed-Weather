package com.lizhizhan.relaxedweather.utils;

import android.widget.Toast;


import com.lizhizhan.relaxedweather.golbal.WeatherApplication;

public class ToastUtil {
    private static Toast toast;

    /**
     * 能够连续弹的吐司，不会等上个吐司消失
     *
     * @param text
     */
    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(WeatherApplication.getInstance(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);//将text文本设置给吐司
        toast.show();
    }
}
