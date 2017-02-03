package com.lizhizhan.relaxedweather.rx;

import com.jakewharton.rxbinding.internal.Preconditions;
import com.lizhizhan.relaxedweather.golbal.WeatherApplication;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpProvider {

    private final static long DEFAULT_TIMEOUT = 5;
    private final static long CONNECT_TIMEOUT = 5;

    public static OkHttpClient getDefaultOkHttpClient() {
        return getNoCacheOkHttpClient(new CacheControlInterceptor());
    }

    public static OkHttpClient getNoCacheOkHttpClient() {
        return getNoCacheOkHttpClient(new FromNetWorkControlInterceptor());
    }

    private static OkHttpClient getNoCacheOkHttpClient(Interceptor cacheControl) {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //设置超时时间
        httpClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        //设置缓存
        File httpCacheDirectory = new File(WeatherApplication.getInstance().getCacheDir(), "OkHttpCache");
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 100 * 1024 * 1024));
        //设置拦截器
        httpClientBuilder.addInterceptor(new LoggingInterceptor());
        httpClientBuilder.addNetworkInterceptor(cacheControl);
        httpClientBuilder.addInterceptor(new UserAgentInterceptor("Android Device"));
        if (cacheControl.getClass().getName().equals("FromNetWorkControlInterceptor")) {
            //这种方法和第一种方法的区别是在设置的拦截器上，这里不能使用NetworkInterceptor，
            // 而是需要使用Interceptor，（不知道为什么用NetworkInterceptor就不行）
            httpClientBuilder.addInterceptor(cacheControl);
        }
        return httpClientBuilder.build();
    }

    private static class CacheControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //网络未连接
            if (!NetworkUtil.isConnected(WeatherApplication.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }

            Response response = chain.proceed(request);

            if (NetworkUtil.isConnected(WeatherApplication.getInstance())) {
                int maxAge = 60 * 60;//一小时
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 30;// 无网络时，设置超时为30天
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }

    /**
     * 离线可以缓存，在线就获取最新数据
     */
    private static class FromNetWorkControlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build();

            Response response = chain.proceed(request);

            if (NetworkUtil.isConnected(WeatherApplication.getInstance())) {
                //                int maxAge = 60 * 1;//设置1分钟的缓存
                //                response = response.newBuilder()
                //                        .header("Cache-Control", "public, max-age=" + maxAge)
                //                        .removeHeader("Pragma")
                //                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return response;
        }
    }

    private static class UserAgentInterceptor implements Interceptor {
        private static final String USER_AGENT_HEADER_NAME = "User-Agent";
        private final String userAgentHeaderValue;

        UserAgentInterceptor(String userAgentHeaderValue) {
            this.userAgentHeaderValue = Preconditions.checkNotNull(userAgentHeaderValue, "userAgentHeaderValue = null");
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            final Request originalRequest = chain.request();
            final Request requestWithUserAgent = originalRequest.newBuilder()
                    .removeHeader(USER_AGENT_HEADER_NAME)
                    .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Logger.i(String.format(Locale.CHINA, "Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Logger.i(String.format(Locale.CHINA, "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
