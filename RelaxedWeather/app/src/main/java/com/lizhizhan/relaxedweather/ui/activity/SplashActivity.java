package com.lizhizhan.relaxedweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.lizhizhan.relaxedweather.MainActivity;
import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.golbal.WeatherApplication;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.PrefUitl;
import com.lizhizhan.relaxedweather.utils.StringUtils;
import com.lizhizhan.relaxedweather.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 闪屏页面。用来获取定位，申请权限，（以及检查更新，未实现）。
 * Created by lizhizhan on 2017/1/12.
 */

public class SplashActivity extends CheckPermissionsActivity {

    @InjectView(R.id.enter_main)
    TextView enterMain;
    private Subscription subscribe;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);

    }

    @Override
    protected void onResume() {
        super.onResume();   //缺少权限时，进入权限设置页面
        //        isFirstEnter = PrefUitl.getBoolean(this, "is_First_Enter", true);
        //        if (!isFirstEnter) {
        //            Logger.e("定位");
        //            initLocation();
        //        }
        //复制各省市信息数据库
        copyDB("city.db");
        //        Logger.e("定位");
        initLocation();
    }

    @Override
    protected void noPermissionsRequest() {
        startMain();
    }

    @Override
    public void startLocationWork() {
        //根据控件的选择，重新设置定位参数
        //        resetOption();
        // 设置定位参数
        //        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }


    /**
     * 复制assets里的数据库
     *
     * @param dbName
     */
    public void copyDB(String dbName) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        File file = new File(getFilesDir(), dbName);
        if (file.exists()) {
            Logger.e(dbName + "数据库已存在");
        } else {
            try {
                Logger.e(dbName + "数据库开始复制");
                //TODO 得到assets目录下的数据库，化为输入流
                inputStream = getAssets().open(dbName);
                outputStream = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = new AMapLocationClientOption();

    /**
     * 得到定位
     */
    public void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            Logger.e("定位回调");
            if (null != loc) {
                //解析定位结果
                String result = Utils.getLocationStr(loc);
                int errorCode = loc.getErrorCode();

                String city = loc.getCity();
                //得到区
                String district = loc.getDistrict();
                if (!StringUtils.isEmpty(district)) {
                    Logger.e("定位成功，loc is " + district + errorCode + city);
                    PrefUitl.setString(WeatherApplication.getInstance(), "location", district);
                }

            } else {
                Logger.e("定位失败，loc is null");

            }
            startMain();
        }
    };


    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }


    // 根据控件的选择，重新设置定位参数
    private void resetOption() {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(true);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(true);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(true);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        String strInterval = "2000";
        if (!TextUtils.isEmpty(strInterval)) {
            try {
                // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
                locationOption.setInterval(Long.valueOf(strInterval));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        String strTimeout = "30000";
        if (!TextUtils.isEmpty(strTimeout)) {
            try {
                // 设置网络请求超时时间
                locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进入首页
     */
    private void startMain() {

        ScaleAnimation animation = new ScaleAnimation(1, 0f, 1, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        enterMain.setAnimation(animation);
        animation.setFillAfter(true);
        animation.start();

        subscribe = Observable.timer(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation() {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    @Override
    protected void onDestroy() {
        subscribe.unsubscribe();
        stopLocation();
        destroyLocation();
        super.onDestroy();
    }

    @OnClick(R.id.enter_main)
    public void onClick() {
        startLocationWork();
        startMain();
    }
}
