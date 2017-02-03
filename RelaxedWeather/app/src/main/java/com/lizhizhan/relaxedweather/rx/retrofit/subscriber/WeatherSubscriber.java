package com.lizhizhan.relaxedweather.rx.retrofit.subscriber;


import com.lizhizhan.relaxedweather.rx.retrofit.HttpResult;
import com.lizhizhan.relaxedweather.utils.StringUtils;

import rx.Subscriber;

/**
 * Created by lizhizhan on 2017/1/10.
 */

public abstract class WeatherSubscriber<T> extends Subscriber<HttpResult<T>> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();
            //在这里做全局的错误处理
            //            if (e instanceof HttpException) {
            //                // ToastUtils.getInstance().showToast(e.getMessage());
            //            }
            if (e.getMessage() == null) {//e.getMessage()可能为null
                _onError(new Throwable(e.toString()));
            } else {
                _onError(e);
            }
        } else {
            _onError(new Exception("null message"));
        }
    }

    @Override
    public void onNext(HttpResult<T> t) {
        if (t.HeWeather5 != null) {
            onSuccess(t.HeWeather5);
        }
    }

    public abstract void onSuccess(T t);

    public abstract void _onError(Throwable e);
}
