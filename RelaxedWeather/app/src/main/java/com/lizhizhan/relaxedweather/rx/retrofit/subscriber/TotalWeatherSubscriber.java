package com.lizhizhan.relaxedweather.rx.retrofit.subscriber;

import rx.Subscriber;

/**
 * Created by lizhizhan on 2017/1/10.
 */

public abstract class TotalWeatherSubscriber<T> extends Subscriber<T> {
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
    public void onNext(T t) {
        if (t != null) {
            onSuccess(t);
        }
    }

    public abstract void onSuccess(T t);

    public abstract void _onError(Throwable e);
}
