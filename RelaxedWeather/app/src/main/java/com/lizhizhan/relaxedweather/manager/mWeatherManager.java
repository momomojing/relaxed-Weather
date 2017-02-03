package com.lizhizhan.relaxedweather.manager;

/**
 * Created by lizhizhan on 2017/1/13.
 */

public class mWeatherManager {

    private static mWeatherManager mWM = new mWeatherManager();
    private WeatherObserver observer;
    //观察者集合
    //    private ArrayList<WeatherObserver> mObservers = new ArrayList<WeatherObserver>();

    public static mWeatherManager getInstance() {
        return mWM;
    }


    /**
     * 注册观察者
     */
    public void registerObserver(WeatherObserver observer) {
        //        if (observer != null && !mObservers.contains(observer)) {
        //            mObservers.add(observer);
        //        }
        this.observer = observer;
    }

    /**
     * 注销观察者
     */
    public void unRegisterObserver(WeatherObserver observer) {
        //        if (observer != null && mObservers.contains(observer)) {
        //            mObservers.remove(observer);
        //        }
        this.observer = observer;
        this.observer = null;
    }

    /**
     * 通知下载状态改变
     *
     * @param weatherInfo
     */
    public void notifynDownloadStateChanged(String weatherInfo, String city) {
        //        for (WeatherObserver observer : mObservers) {
        //            observer.onWeatherStateChanged(weatherInfo, city);
        //        }


        observer.onWeatherStateChanged(weatherInfo, city);
    }


    public synchronized void change(String weatherInfo, String city) {
        notifynDownloadStateChanged(weatherInfo, city);
    }

    /**
     * 声明观察者的接口
     */
    public interface WeatherObserver {
        /**
         * 天气状态的改变
         *
         * @param cond
         * @param city
         */
        public void onWeatherStateChanged(String cond, String city);

    }
}
