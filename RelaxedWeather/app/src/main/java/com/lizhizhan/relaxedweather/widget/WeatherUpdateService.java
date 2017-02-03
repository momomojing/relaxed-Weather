package com.lizhizhan.relaxedweather.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.lizhizhan.relaxedweather.MainActivity;
import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.api.getWeather;
import com.lizhizhan.relaxedweather.bean.NowWeatherBean;
import com.lizhizhan.relaxedweather.bean.WeatherBean;
import com.lizhizhan.relaxedweather.db.CurrentCityDao;
import com.lizhizhan.relaxedweather.golbal.WeatherApplication;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.PrefUitl;
import com.lizhizhan.relaxedweather.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Created by lizhizhan on 2017/1/16.
 */
public class WeatherUpdateService extends Service {
    private Context mContext;
    private static final String TAG = "TestAppWidget";
    private static final String FRESH = "com.lizhizhan.relaxedweather.fresh";
    private AppWidgetManager appWidgetManager;

    private Timer timer;
    private TimerTask task;
    public SimpleDateFormat sdf;
    private RemoteViews views;
    private String firstCity;
    private Timer timer2;
    private ComponentName provider;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CurrentCityDao currentCityDao = new CurrentCityDao(WeatherApplication.getInstance());
        ArrayList<String> all = currentCityDao.findAll();
        String location = PrefUitl.getString(WeatherApplication.getInstance(), "location", null);
        /**
         * 第一个表示上下文，第二个表示哪个广播去出来桌面小控件。
         */
        provider = new ComponentName(getApplicationContext(), WeatherWidget.class);
        if (location == null) {
            firstCity = all.get(0);
        } else {
            firstCity = location;
        }
        appWidgetManager = AppWidgetManager.getInstance(this);
        views = new RemoteViews(getPackageName(), R.layout.widget_4x2);
        sdf = new SimpleDateFormat("HH:mm");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String time = sdf.format(new Date());
                updateTime(time);
            }
        }, 0, 1000 * 60);

        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                Logger.e("定时天气");
                new getWeather(firstCity, WeatherApplication.WeatherType_NOW) {
                    @Override
                    protected void onTotalWeatherLoad(WeatherBean weatherBean) {

                    }

                    @Override
                    public void onNowDataLoad(NowWeatherBean nowWeatherBean) {
                        updateWeather(nowWeatherBean);
                    }

                    @Override
                    public void error() {

                    }
                };


            }
        }, 0, 1000 * 60 * 30);

    }

    private void updateTime(String time) {
        Logger.e("更新时间");
        Intent _intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, _intent, 0);
        views.setOnClickPendingIntent(R.id.tv_weather, pendingIntent);//点击跳到主页
        views.setTextViewText(R.id.tv_weather, time);
        views.setTextViewTextSize(R.id.tv_weather, COMPLEX_UNIT_SP, UIUtils.dip2px(20));

        // 更新桌面
        appWidgetManager.updateAppWidget(provider, views);
    }

    private void updateWeather(NowWeatherBean weatherBean) {
        String cond = weatherBean.getHeWeather5().get(0).getNow().getCond().getTxt();
        String tmp = weatherBean.getHeWeather5().get(0).getNow().getTmp();
        Logger.e("更新天气更新天气更新天气更新天气更新天气" + firstCity);
        views.setTextViewText(R.id.tv_city_name, firstCity);
        views.setTextViewText(R.id.tv_cond, cond);
        views.setTextViewText(R.id.tv_tmp, tmp + "°");
        views.setTextViewTextSize(R.id.tv_tmp, COMPLEX_UNIT_SP, UIUtils.dip2px(20));
        /**
         * 第一个表示上下文，第二个表示哪个广播去出来桌面小控件。
         */
        ComponentName provider = new ComponentName(getApplicationContext(), WeatherWidget.class);
        // 更新桌面
        appWidgetManager.updateAppWidget(provider, views);
    }

    private void updateTime() {

    }
}
