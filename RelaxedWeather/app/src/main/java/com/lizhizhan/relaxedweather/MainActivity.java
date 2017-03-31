package com.lizhizhan.relaxedweather;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lizhizhan.relaxedweather.adatper.BaseViewPagerAdapter;
import com.lizhizhan.relaxedweather.db.CurrentCityDao;
import com.lizhizhan.relaxedweather.golbal.WeatherApplication;
import com.lizhizhan.relaxedweather.ui.activity.AchieveActivity;
import com.lizhizhan.relaxedweather.ui.fragment.baseFragment;
import com.lizhizhan.relaxedweather.ui.fragment.leftMenuFragment;
import com.lizhizhan.relaxedweather.ui.fragment.weatherFragment;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.PrefUitl;
import com.lizhizhan.relaxedweather.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.lizhizhan.relaxedweather.golbal.WeatherApplication.CreatNewWeatherPager;
import static com.lizhizhan.relaxedweather.golbal.WeatherApplication.fragmentList;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.vp_weather)
    ViewPager vpWeather;
    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.main_cord)
    CoordinatorLayout mainCord;
    @InjectView(R.id.fl_menu)
    FrameLayout flMenu;
    @InjectView(R.id.activity_main)
    RelativeLayout activityMain;
    @InjectView(R.id.my_Drawer_layout)
    DrawerLayout myDrawerLayout;
    @InjectView(R.id.conter_title_txt)
    TextView conterTitleTxt;
    String lastBgPic = null;
    @InjectView(R.id.ll_point)
    LinearLayout llPoint;
    @InjectView(R.id.iv_bgPic)
    SimpleDraweeView ivBgPic;

    private int mPreviousPos;
    private Bundle weatherBundle;
    private BaseViewPagerAdapter baseViewPagerAdapter;
    private String lastCond = "";
    private Uri uri2;
    private static HashMap<String, baseFragment> mFragmentMap = new HashMap<String, baseFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        PrefUitl.setBoolean(this, "is_First_Enter", false);
        String addNewCity = getIntent().getStringExtra("addNewCity");
        //默认背景色
        ivBgPic.setImageResource(R.color.yin);
        initToolBar();
        initWeatherPager(addNewCity);
        initLeftMenu();

    }

    /**
     * 更新侧滑菜单
     */
    private void initLeftMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        leftMenuFragment leftMenuFragment = new leftMenuFragment();
        //        weatherFragment baseFragment = new weatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("city", "北京");
        leftMenuFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fl_menu, leftMenuFragment, "TAG_LEFT_MENU");
        fragmentTransaction.commit();

    }


    private ArrayList<String> city = new ArrayList<String>();

    /**
     * 更新天气页面的操作
     *
     * @param addNewCity 添加的新城市
     */
    private void initWeatherPager(String addNewCity) {
        String location = PrefUitl.getString(WeatherApplication.getInstance(), "location", null);
        if (location != null) {
            city.add(location);
            Logger.e("定位的城市是" + location);
        }
        CurrentCityDao currentCityDao = new CurrentCityDao(this);
        ArrayList<String> allCity = currentCityDao.findAll();
        if (allCity.size() != 0) {
            //数据库有值
            city.addAll(allCity);
        }
        if (city.size() != 0) {

            PrefUitl.setString(UIUtils.getContext(), "FIRSE_CITY", city.get(0));

            //已经有选择的城市了，可以更新天气
            initPoint(city.size(), 0);
            baseViewPagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), this);

            //       baseViewPagerAdapter.addPager();
            for (int i = 0; i < city.size(); i++) {
                weatherBundle = new Bundle();
                weatherBundle.putString("city", city.get(i));
                Logger.e("创建Fragment" + city.get(i));

                baseViewPagerAdapter.addPager(weatherFragment.class, weatherBundle);

            }
            vpWeather.setAdapter(baseViewPagerAdapter);
            //            fragment.getDate("厦门");

            //限制0没用，默认为1 。
            //            vpWeather.setOffscreenPageLimit(0);

            //默认获取第一页的天气，更新背景
            //            getNowCond(city.get(0));

            if (addNewCity != null) {
                //跳转到新添加的页面
                vpWeather.setCurrentItem(city.size());
                int currentItem = vpWeather.getCurrentItem();

                //新添加的天气，更新背景
                //                getNowCond(addNewCity);

                //添加小白点
                initPoint(city.size(), currentItem);

                mPreviousPos = currentItem;
                //当前所在页面小白点设置为白色的
            }
            //第一次进入时候，设置定位页面的标题
            int currentItem = vpWeather.getCurrentItem();
            if (currentItem >= 0) {
                conterTitleTxt.setText(city.get(currentItem));
            } else {
                conterTitleTxt.setText("请选择城市");
            }
            vpWeather.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                    //页面滑动过程,positionOffset滑动比例，0到1
                }

                @Override
                public void onPageSelected(int position) {


                    weatherFragment fragment = fragmentList.get(position);
//                    fragment.onLoad();
                    fragment.lazyLoad();
                    conterTitleTxt.setText(city.get(position));
                    //当前所在页面小白点设置为白色的
                    ImageView point = (ImageView) llPoint.getChildAt(position);
                    point.setImageResource(R.drawable.shape_point_white);

                    //上个点设为不选择状态
                    ImageView prePoint = (ImageView) llPoint.getChildAt(mPreviousPos);
                    prePoint.setImageResource(R.drawable.shape_point_gray);
                    mPreviousPos = position;

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            //跳转到选择天气的页面
            Intent intent = new Intent(this, AchieveActivity.class);
            intent.putExtra("category", CreatNewWeatherPager);
            startActivity(intent);
        }
    }

    /**
     * 根据天气情况更新背景
     *
     * @param cond
     */
    private void initBgPic(String cond) {
        if (!lastCond.equals(cond)) {
            lastCond = cond;
            switch (cond) {
                case "多云":
                    Logger.e(cond);
                    uri2 = Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.duoyun);
                    break;
                case "晴":
                    Logger.e(cond);
                    uri2 = Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.sun3);
                    break;
                case "小雨":
                    uri2 = Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.rain2);
                    Logger.e(cond);
                    break;
                case "霾":
                    Logger.e(cond);
                    break;
                case "阴":
                    uri2 = Uri.parse("res://" + this.getPackageName() + "/" + R.drawable.yin);
                    Logger.e(cond);
                    break;
            }
            DraweeController draweeController =
                    Fresco.newDraweeControllerBuilder()
                            .setUri(uri2)
                            .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                            .build();
            ivBgPic.setController(draweeController);
        }

    }

    /**
     * 添加小白点
     *
     * @param citySize
     * @param currentPos 当前选中的白点
     */

    private void initPoint(int citySize, int currentPos) {
        llPoint.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < citySize; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setBackgroundResource(R.drawable.shape_point_gray);
            imageView.setLayoutParams(layoutParams);
            if (i == currentPos) {
                imageView.setBackgroundResource(R.drawable.shape_point_white);
            }
            if (i > 0)
                layoutParams.leftMargin = UIUtils.dip2px(5);
            llPoint.addView(imageView);
        }
    }

    private Bundle getBundle(String city) {
        Bundle bundle = new Bundle();
        bundle.putString("city", city);
        return bundle;
    }


    private void initToolBar() {
        myToolbar.setTitle("");
        //        myToolbar.setSubtitle("你好啊");
        conterTitleTxt.setText("");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, myDrawerLayout, myToolbar, R.string.open, R.string.close) {


            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Logger.e("关闭");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Logger.e("打开");
            }
        };
        toggle.syncState();
        myDrawerLayout.setDrawerListener(toggle);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //当侧面板是打开的状态的情况下，关闭侧面板。
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myDrawerLayout.isDrawerOpen(flMenu)) {
                myDrawerLayout.closeDrawers();
            } else {
                //点击返回键退回桌面，而不是退出运用
                Intent backHome = new Intent(Intent.ACTION_MAIN);
                backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                backHome.addCategory(Intent.CATEGORY_HOME);
                startActivity(backHome);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 得到天气变化的回调
     *
     * @param condChanged
     */
    public void setCondChanged(String condChanged) {
        Logger.e("主页面得到天气变化" + condChanged);
        initBgPic(condChanged);
    }
}
