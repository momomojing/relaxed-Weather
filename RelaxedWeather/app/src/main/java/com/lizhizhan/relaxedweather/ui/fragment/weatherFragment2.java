package com.lizhizhan.relaxedweather.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.lizhizhan.relaxedweather.api.getWeather;
import com.lizhizhan.relaxedweather.bean.NowWeatherBean;
import com.lizhizhan.relaxedweather.bean.WeatherBean;
import com.lizhizhan.relaxedweather.golbal.WeatherApplication;
import com.lizhizhan.relaxedweather.ui.view.LoadingPage;
import com.lizhizhan.relaxedweather.ui.view.WeatherHolder;
import com.lizhizhan.relaxedweather.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lizhizhan on 2017/1/11.
 */

public class weatherFragment2 extends baseFragment {


    private View view;
    private String city;

    private SharedPreferences Confing;
    private WeatherBean weatherBean;
    private List<WeatherBean.HeWeather5Bean> heWeather5;
    private WeatherHolder weatherHolder;

    @Override
    public View OnCreatSuccessView() {

        weatherHolder = new WeatherHolder(this, context);
        view = weatherHolder.getmRootView();
        weatherHolder.setData(weatherBean);
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        Logger.e("当前城市是" + city);
        getWeather getWeather = new getWeather(city, WeatherApplication.WeatherType_Total) {
            @Override
            protected void onTotalWeatherLoad(WeatherBean weatherBean) {
                weatherFragment2.this.weatherBean = weatherBean;
                heWeather5 = weatherBean.getHeWeather5();
                if (weatherHolder != null) {

                    weatherHolder.setData(weatherBean);
                }

            }

            @Override
            public void onNowDataLoad(NowWeatherBean nowWeatherBean) {

            }

            @Override
            public void error() {

            }
        };

        return check(heWeather5);
        //        return LoadingPage.ResultState.STATE_ERROR;
    }

    /**
     * 对返回的网络数据进行校检
     *
     * @param obj
     * @return
     */
    public LoadingPage.ResultState check(Object obj) {

        if (obj != null) {
            if (obj instanceof ArrayList) {
                ArrayList list = (ArrayList) obj;
                if (list.isEmpty()) {
                    return LoadingPage.ResultState.STATE_EMPTY;
                } else {
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }
            }

        }
        return LoadingPage.ResultState.STATE_ERROR;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        city = bundle.getString("city");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
