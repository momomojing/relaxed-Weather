package com.lizhizhan.relaxedweather.api;

import com.lizhizhan.relaxedweather.bean.NowWeatherBean;
import com.lizhizhan.relaxedweather.bean.WeatherBean;
import com.lizhizhan.relaxedweather.rx.retrofit.HttpResult;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by lizhizhan on 2017/1/10.
 */

public interface WeartherService {
    String BASE_URL = "https://free-api.heweather.com/";

    @GET("v5/now")
    Observable<NowWeatherBean> getNowWeathers(@QueryMap Map<String, Object> options);

    @GET("v5/weather")
    Observable<WeatherBean> getTotalWeathers(@QueryMap Map<String, Object> options);

}
