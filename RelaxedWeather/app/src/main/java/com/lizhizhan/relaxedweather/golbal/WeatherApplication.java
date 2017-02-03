package com.lizhizhan.relaxedweather.golbal;

import android.app.Application;
import android.os.Environment;
import android.os.Handler;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.File;

/**
 * Created by lizhizhan on 2017/1/10.
 */

public class WeatherApplication extends Application {
    private static WeatherApplication instance;
    public static String WeatherType_NOW = "NOW";
    public static String WeatherType_Total = "TOTAL";
    public static String WeatherType_ALL = "ALL";

    public static int CreatNewWeatherPager = 0;

    private static Handler handler;
    private static int mainThreadid;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        instance = this;
        handler = new Handler();
        //当前线程id，此处是主线程id
        mainThreadid = android.os.Process.myTid();


        //        initImageLoader(instance);
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadid() {
        return mainThreadid;
    }
    //    private void initImageLoader(Context context) {
    //        File cacheDir = StorageUtils.getCacheDirectory(context);  //缓存文件夹路径
    //        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
    //        config.threadPriority(Thread.NORM_PRIORITY - 2);
    //        config.denyCacheImageMultipleSizesInMemory();
    //        config.diskCacheFileNameGenerator(new Md5FileNameGenerator()); //将保存的时候的URI名称用MD5 加密
    //        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB sd卡(本地)缓存的最大值
    //        config.tasksProcessingOrder(QueueProcessingType.LIFO);
    //        config.writeDebugLogs(); // Remove for release app
    //        config.diskCache(new UnlimitedDiskCache(cacheDir));//自定义缓存路径
    //        //        config.threadPoolSize(3);//线程池内加载的数量
    //        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000));// connectTimeout (5 s), readTimeout (30 s)超时时间
    //        // Initialize ImageLoader with configuration.
    //        ImageLoader.getInstance().init(config.build());
    //    }

    public static WeatherApplication getInstance() {
        return instance;
    }

    @Override
    public File getCacheDir() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir;
            }
        }

        return super.getCacheDir();
    }
}
