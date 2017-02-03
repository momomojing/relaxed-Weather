package com.lizhizhan.relaxedweather.rx.retrofit.service;


import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by lizhizhan on 2016/12/19.
 */

public interface JieMengLocalService {

    String BASE_URL = "http://192.168.191.1:8080/";

//    @GET("jiemeng/jiemeng.json")
//    Observable<HttpResult<List<JieMengDataBean>>> Query();
}
