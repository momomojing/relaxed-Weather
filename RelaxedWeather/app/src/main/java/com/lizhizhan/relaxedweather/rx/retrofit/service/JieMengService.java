package com.lizhizhan.relaxedweather.rx.retrofit.service;


import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lizhizhan on 2016/12/19.
 */

public interface JieMengService {

    String BASE_URL = "http://apis.haoservice.com/lifeservice/dream/";
    //    http://apis.haoservice.com/lifeservice/dream/query?key=095a385055f44d7c86da03ec15e68d96&q=%E7%8B%97&pageSize=20
//    @GET("query")
//    Observable<HttpResult<List<JieMengDataBean>>> Query(@Query("key") String key, @Query("q") String q);
}
