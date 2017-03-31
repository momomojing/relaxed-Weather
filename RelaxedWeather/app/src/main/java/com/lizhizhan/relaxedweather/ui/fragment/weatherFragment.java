package com.lizhizhan.relaxedweather.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.MainActivity;
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
 * Created by lizhizhan on 2017/3/20.
 */

public class weatherFragment extends Fragment {

    private static final int SUCCESS = 1;
    private static final int ERROR = 0;
    @InjectView(R.id.tv)
    TextView tv;
    @InjectView(R.id.tmp)
    TextView tmp;
    @InjectView(R.id.iv_condIcon)
    ImageView ivCondIcon;
    @InjectView(R.id.cond)
    TextView cond;
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
    @InjectView(R.id.nScrollView)
    ScrollView nScrollView;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private Context context;
    private List<WeatherBean.HeWeather5Bean> heWeather5;
    private String city;
    private String condString;
    String base = "http://files.heweather.com/cond_icon/";

    private boolean isFirstEnter;
    private SharedPreferences Confing;
    private int measuredHeight;
    private RelativeLayout.LayoutParams llMoreLayoutParams;
    private MainActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        Logger.e("调用新的fragment");
        context = getContext();
        Bundle bundle = getArguments();
        city = bundle.getString("city");
        Logger.e("当前城市是" + city);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isLoaded = false;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            lazyLoad();
        }
    }

    public void lazyLoad() {
        if (heWeather5 == null) {
            onLoad();
        } else {
            initView(heWeather5);
            if (!isLoaded) {
                onLoad();
            } else if (heWeather5 != null) {
                initView(heWeather5);
            }
        }

    }

    public void onLoad() {
        isLoaded = true;
        Logger.e("当前城市是" + city);
        getWeather getWeather = new getWeather(city, WeatherApplication.WeatherType_Total) {
            @Override
            protected void onTotalWeatherLoad(WeatherBean weatherBean) {
                heWeather5 = weatherBean.getHeWeather5();
                if (heWeather5 != null) {
                    initView(heWeather5);
                    Message message = new Message();
                    //                    message.setData();
                    message.what = SUCCESS;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onNowDataLoad(NowWeatherBean nowWeatherBean) {

            }

            @Override
            public void error() {
                Message message = new Message();
                //                    message.setData();
                message.what = ERROR;
                handler.sendMessage(message);
            }
        };
    }

    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Snackbar.make(refreshLayout, "刷新成功", Snackbar.LENGTH_LONG).show();
                    refreshLayout.setRefreshing(false);
                    break;
                case ERROR:
                    Snackbar.make(refreshLayout, "刷新失败", Snackbar.LENGTH_LONG).show();
                    refreshLayout.setRefreshing(false);
                    break;
            }
            return false;
        }
    });

    /**
     * 更新视图
     *
     * @param weatherBean
     */
    private void initView(List<WeatherBean.HeWeather5Bean> weatherBean) {
        WeatherBean.HeWeather5Bean heWeather5Bean = weatherBean.get(0);
        final WeatherBean.HeWeather5Bean.NowBean now = heWeather5Bean.getNow();
        List<WeatherBean.HeWeather5Bean.DailyForecastBean> dailyForecast = heWeather5Bean.getDaily_forecast();
        Logger.e("更新UI" + city);
        Logger.e(heWeather5Bean.getBasic().getCity());

        tv.setText(heWeather5Bean.getBasic().getCity());

        tmp.setText(now.getTmp());

        condString = now.getCond().getTxt();

        cond.setText(condString);
        activity.setCondChanged(condString);

        WeatherBean.HeWeather5Bean.DailyForecastBean dailyForecastBean = dailyForecast.get(0);
        WeatherBean.HeWeather5Bean.DailyForecastBean.AstroBean astro = dailyForecastBean.getAstro();
        tvHum.setText("湿度" + dailyForecastBean.getHum() + "%");
        tvSr.setText("日出时间：" + astro.getSr());
        tvSs.setText("日落时间：" + astro.getSs());
        //体感温度
        final String fl = now.getFl();
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
                Logger.e("点击了");
                toggle();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.e("刷新啊啊啊啊啊啊啊啊啊啊啊");
                onLoad();
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
            Drawable drawable = context.getResources().getDrawable(R.drawable.arrow_down);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tvMore.setCompoundDrawables(drawable, null, null, null);
            isOpenMore = false;
            //设置成关
            valueAnimator = ValueAnimator.ofInt(measuredHeight, 0);
            llMore.setVisibility(View.INVISIBLE);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.arrow_up);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
