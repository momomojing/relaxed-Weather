package com.lizhizhan.relaxedweather.api;

import com.lizhizhan.relaxedweather.bean.NowWeatherBean;
import com.lizhizhan.relaxedweather.bean.WeatherBean;
import com.lizhizhan.relaxedweather.rx.retrofit.TransformUtils;
import com.lizhizhan.relaxedweather.rx.retrofit.factory.ServiceFactory;
import com.lizhizhan.relaxedweather.rx.retrofit.subscriber.TotalWeatherSubscriber;
import com.lizhizhan.relaxedweather.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhizhan on 2017/1/10.
 */

public abstract class getWeather {
    String key = "8b08acfac6e94abcb6658b229d2e44fc";

    public getWeather(String city, String weatherType) {
        switch (weatherType) {
            case "NOW":
                getNowWeather(city);
                break;
            case "TOTAL":
                getTotalWeatherBean(city);
                break;

            case "ALL":
                getNowWeather(city);
                getTotalWeatherBean(city);
                break;

        }

    }

    public void getNowWeather(String city) {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("city", city);
        options.put("key", key);
        WeartherService WeartherService = ServiceFactory.getInstance().createService(WeartherService.class);
        WeartherService.getNowWeathers(options).compose(TransformUtils.<NowWeatherBean>defaultSchedulers())
                .subscribe(new TotalWeatherSubscriber<NowWeatherBean>() {


                    @Override
                    public void onSuccess(NowWeatherBean nowWeatherBean) {
                        if (nowWeatherBean != null) {
                            onNowDataLoad(nowWeatherBean);
                        } else {
                            error();
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        Logger.e("失败了啊");
                        error();
                    }
                });
    }


    public void getTotalWeatherBean(String city) {
        Map<String, Object> options = new HashMap<String, Object>();
        options.put("city", city);
        options.put("key", key);
        WeartherService WeartherService = ServiceFactory.getInstance().createService(WeartherService.class);
        WeartherService.getTotalWeathers(options).compose(TransformUtils.<WeatherBean>defaultSchedulers())
                .subscribe(new TotalWeatherSubscriber<WeatherBean>() {
                    @Override
                    public void onSuccess(WeatherBean weatherBean) {
                        if (weatherBean != null) {
                            onTotalWeatherLoad(weatherBean);
                        } else {
                            Logger.e("失败了啊+80行" + getClass().getName());
                            error();
                        }
                    }

                    @Override
                    public void _onError(Throwable e) {
                        Logger.e("失败了啊+84行" + getClass().getName());
                        error();
                    }
                });
    }

    /**
     * 天气的所有信息
     *
     * @param weatherBean
     */
    protected abstract void onTotalWeatherLoad(WeatherBean weatherBean);

    /**
     * 当前天气的情况
     *
     * @param nowWeatherBean
     */
    public abstract void onNowDataLoad(NowWeatherBean nowWeatherBean);

    public abstract void error();
}
