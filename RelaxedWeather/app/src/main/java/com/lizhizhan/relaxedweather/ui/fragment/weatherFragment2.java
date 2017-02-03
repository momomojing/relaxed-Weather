package com.lizhizhan.relaxedweather.ui.fragment;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.api.getWeather;
import com.lizhizhan.relaxedweather.bean.NowWeatherBean;
import com.lizhizhan.relaxedweather.bean.WeatherBean;
import com.lizhizhan.relaxedweather.golbal.WeatherApplication;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lizhizhan on 2017/1/11.
 */

public class weatherFragment2 extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.tv)
    TextView tv;
    @InjectView(R.id.tmp)
    TextView tmp;
    @InjectView(R.id.cond)
    TextView cond;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.iv_condIcon)
    ImageView ivCondIcon;
    @InjectView(R.id.Htmp)
    TextView Htmp;
    @InjectView(R.id.Ltmp)
    TextView Ltmp;
    @InjectView(R.id.ll_tmp)
    LinearLayout llTmp;
    @InjectView(R.id.tv_hum)
    TextView tvHum;
    @InjectView(R.id.tv_spd)
    TextView tvSpd;
    @InjectView(R.id.tv_more)
    TextView tvMore;
    @InjectView(R.id.ll_hum_speed)
    LinearLayout llHumSpeed;
    @InjectView(R.id.tv_sport)
    TextView tvSport;
    @InjectView(R.id.tv_uv)
    TextView tvUv;
    @InjectView(R.id.tv_sr)
    TextView tvSr;
    @InjectView(R.id.tv_ss)
    TextView tvSs;
    @InjectView(R.id.tv_fl)
    TextView tvFl;
    @InjectView(R.id.tv_comf)
    TextView tvComf;
    @InjectView(R.id.ll_more)
    LinearLayout llMore;
    @InjectView(R.id.nScrollView)
    ScrollView nScrollView;
    @InjectView(R.id.iv_condIconToday)
    ImageView ivCondIconToday;
    @InjectView(R.id.tv_toHtmp)
    TextView tvToHtmp;
    @InjectView(R.id.tv_toLtmp)
    TextView tvToLtmp;
    @InjectView(R.id.iv_condIconTom)
    ImageView ivCondIconTom;
    @InjectView(R.id.tv_tomHtmp)
    TextView tvTomHtmp;
    @InjectView(R.id.tv_tomLtmp)
    TextView tvTomLtmp;
    @InjectView(R.id.iv_condIconAfterTom)
    ImageView ivCondIconAfterTom;
    @InjectView(R.id.tv_afterTomHtmp)
    TextView tvAfterTomHtmp;
    @InjectView(R.id.tv_afterTomLtmp)
    TextView tvAfterTomLtmp;
    @InjectView(R.id.ll_furture)
    LinearLayout llFurture;
    private Context context;
    private View view;
    private String city;

    private boolean isFirstEnter;
    private SharedPreferences Confing;
    private int measuredHeight;
    private RelativeLayout.LayoutParams llMoreLayoutParams;
    String base = "http://files.heweather.com/cond_icon/";

    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    private String condString = "未赋值";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //避免切换Fragment 的时候重绘UI 。失去数据
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_content, null);
        }
        // 缓存的viewiew需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.inject(this, view);
        isCreateView = true;
        return view;
    }

    /**
     * 第一个Fragment 在onActivityCreated 调用
     * 第一个之后的Fragment 会从这里加载数据
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //界面对用户可见时候
        if (isVisibleToUser && isCreateView) {
            //            lazyLoad();
        }
    }

    public void getDate(String city2) {
        initWeatherData(city2);
        Logger.e(" getDate()getDate()getDate()getDate()");
    }

    /**
     * 懒加载Fragment 的 UI 对用户可见时才加载数据
     * 可见时候，观察者模式调用，更新天气状态。根据天气状态更改运用背景
     */
    private void lazyLoad() {
        //如果没有加载过就加载，否则就不再加载了
        if (!isLoadData) {
            //加载数据操作
            onRefresh();

            Logger.e(" wearF 173+刷新数据");

            isLoadData = true;
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //第一个fragment会调用
        if (getUserVisibleHint()) {
            //            lazyLoad();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout.setOnRefreshListener(this);
        tv.setText(city);
                lazyLoad();
    }


    /**
     * 获得天气
     *
     * @param city
     */
    private void initWeatherData(String city) {

        new getWeather(city, WeatherApplication.WeatherType_Total) {
            @Override
            protected void onTotalWeatherLoad(WeatherBean weatherBean) {
                stopRefresh();
                WeatherBean.HeWeather5Bean heWeather5Bean = weatherBean.getHeWeather5().get(0);
                Logger.e(heWeather5Bean.getBasic().getUpdate().getLoc());
                initView(heWeather5Bean);

            }

            @Override
            public void onNowDataLoad(NowWeatherBean nowWeatherBean) {
                stopRefresh();
            }

            @Override
            public void error() {
                Logger.e("errorerrorerrorerrorerrorerror");
                stopRefresh();
                //提示没网。连接出错
                Snackbar.make(refreshLayout, "没网噢", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        };


    }

    /**
     * 更新天气状况
     */
    @Override
    public void onRefresh() {
        Logger.e("刷新刷新刷新刷新刷新刷新刷新刷新刷新刷新刷新刷新");
        initWeatherData(city);
        //        mWeatherManager mWM = mWeatherManager.getInstance();
        //        mWM.change(condString, city);

        //        if (NetworkUtil.isConnected(context)) {
        //            initWeatherData(city);
        //            mWeatherManager mWM = mWeatherManager.getInstance();
        //            mWM.change(condString, city);
        //        } else {
        //            stopRefresh();
        //            Snackbar.make(refreshLayout, "检测到没网", Snackbar.LENGTH_SHORT).setAction("知道了", new View.OnClickListener() {
        //                @Override
        //                public void onClick(View v) {
        //
        //                }
        //            }).show();
        //
        //        }
    }

    /**
     * 停止刷新
     */
    protected void stopRefresh() {
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
            Log.e("tag", "stopRefresh: ");
        }
    }

    /**
     * 获得天气后更新视图
     *
     * @param heWeather5Bean
     */
    private void initView(WeatherBean.HeWeather5Bean heWeather5Bean) {

        final WeatherBean.HeWeather5Bean.NowBean now = heWeather5Bean.getNow();
        List<WeatherBean.HeWeather5Bean.DailyForecastBean> dailyForecast = heWeather5Bean.getDaily_forecast();
        Logger.e("更新UI");
        Logger.e(heWeather5Bean.getBasic().getCity());
        tmp.setText(now.getTmp());

        condString = now.getCond().getTxt();


        //        mWeatherManager mWM = mWeatherManager.getInstance();
        //        mWM.change(condString, city);


        cond.setText(condString);
        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean = dailyForecast.get(0);
        WeatherBean.HeWeather5Bean.DailyForecastBean.AstroBean astro = dailyForecastBean.getAstro();
        tvHum.setText("湿度" + dailyForecastBean.getHum() + "%");
        tvSr.setText("日出时间：" + astro.getSr());
        tvSs.setText("日落时间：" + astro.getSs());
        //体感温度
        String fl = now.getFl();
        tvFl.setText("体感温度:" + fl + "°");

        //风速
        tvSpd.setText("风速：" + now.getWind().getSpd() + "km/h");
        //运动指数
        tvSport.setText("运动指数：" + heWeather5Bean.getSuggestion().getSport().getBrf());
        //最高温
        Htmp.setText(dailyForecastBean.getTmp().getMax());
        //最低温
        Ltmp.setText(dailyForecastBean.getTmp().getMin());

        tvUv.setText("紫外线指数：" + heWeather5Bean.getSuggestion().getUv().getBrf());
        //舒适度
        tvComf.setText("舒适度指数：" + heWeather5Bean.getSuggestion().getComf().getBrf());
        //        ivCondIcon.setImageResource();

        String code = now.getCond().getCode();
        String url = base + code + ".png";
        Picasso.with(context).load(url).into(ivCondIcon);

        initFutuerWeather(heWeather5Bean.getDaily_forecast());


        measuredHeight = llMore.getMeasuredHeight();
        llMoreLayoutParams = (RelativeLayout.LayoutParams) llMore.getLayoutParams();

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    /**
     * 未来几天预告
     *
     * @param daily_forecast
     */
    private void initFutuerWeather(List<WeatherBean.HeWeather5Bean.DailyForecastBean> daily_forecast) {
        WeatherBean.HeWeather5Bean.DailyForecastBean today = daily_forecast.get(0);
        tvToHtmp.setText(today.getTmp().getMax() + "°");
        tvToLtmp.setText(today.getTmp().getMin() + "°");
        Picasso.with(context).load(base + today.getCond().getCode_n() + ".png").into(ivCondIconToday);
        WeatherBean.HeWeather5Bean.DailyForecastBean tom = daily_forecast.get(1);
        tvTomHtmp.setText(tom.getTmp().getMax() + "°");
        tvTomLtmp.setText(tom.getTmp().getMin() + "°");
        Picasso.with(context).load(base + tom.getCond().getCode_n() + ".png").into(ivCondIconTom);
        WeatherBean.HeWeather5Bean.DailyForecastBean afterTom = daily_forecast.get(2);
        tvAfterTomHtmp.setText(afterTom.getTmp().getMax() + "°");
        tvAfterTomLtmp.setText(afterTom.getTmp().getMin() + "°");
        Picasso.with(context).load(base + afterTom.getCond().getCode_n() + ".png").into(ivCondIconAfterTom);

    }

    private boolean isOpenMore = true;

    /**
     * 更多信息的打开与关闭
     */
    private void toggle() {
        ValueAnimator valueAnimator = null;
        if (isOpenMore) {
            Drawable drawable = getResources().getDrawable(R.drawable.arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvMore.setCompoundDrawables(drawable, null, null, null);
            isOpenMore = false;
            //设置成关
            valueAnimator = ValueAnimator.ofInt(measuredHeight, 0);
            llMore.setVisibility(View.INVISIBLE);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.arrow_up);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvMore.setCompoundDrawables(drawable, null, null, null);
            isOpenMore = true;
            //设置成开
            valueAnimator = ValueAnimator.ofInt(0, measuredHeight);
            llMore.setVisibility(View.VISIBLE);
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取最新的高度
                Integer heigh = (Integer) animation.getAnimatedValue();
                llMoreLayoutParams.height = heigh;
                llMore.setLayoutParams(llMoreLayoutParams);
            }
        });
        //设置时间间隔
        valueAnimator.setDuration(200);
        //        开始动画
        valueAnimator.start();
    }

    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }


    private void onAttachToContext(Context context) {

    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


}
