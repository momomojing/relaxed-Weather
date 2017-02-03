package com.lizhizhan.relaxedweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.MainActivity;
import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.bean.CondEvent;
import com.lizhizhan.relaxedweather.ui.fragment.AddCityFragment;
import com.lizhizhan.relaxedweather.ui.fragment.aboutFragment;
import com.lizhizhan.relaxedweather.ui.fragment.citysFragment;
import com.lizhizhan.relaxedweather.ui.fragment.settingFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 侧面板的点击跳转，实现具体功能的Activity
 * Created by lizhizhan on 2017/1/14.
 */

public class AchieveActivity extends AppCompatActivity {
    @InjectView(R.id.my_toolbar)
    Toolbar myToolbar;
    @InjectView(R.id.fl_achieve)
    FrameLayout flAchieve;
    @InjectView(R.id.conter_title_txt)
    TextView conterTitleTxt;
    private int intExtra;
    private Boolean cond = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_achieve);
        ButterKnife.inject(this);

        EventBus.getDefault().register(this);

        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//显示标题
        intExtra = getIntent().getIntExtra("category", -1);
        initFragment(intExtra);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (cond) {
                    Intent intent = new Intent(AchieveActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                //切换抽屉
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //返回时，若城市列表有更新则，打开主页面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (cond) {
                Intent intent = new Intent(AchieveActivity.this, MainActivity.class);
                startActivity(intent);
            }
            //切换抽屉
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initFragment(int intExtra) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = null;
        if (intExtra != -1) {
            switch (intExtra) {
                case 0:
                    //添加
                    conterTitleTxt.setText("添加");
                    fragment = new AddCityFragment();
                    bundle = new Bundle();
                    bundle.putString("city", "北京");
                    break;
                case 1:
                    //设置
                    conterTitleTxt.setText("地点");
                    fragment = new citysFragment();
                    break;
                case 2:
                    //关于
                    conterTitleTxt.setText("设置");
                    fragment = new settingFragment();
                    break;
                case 3:
                    //关于
                    conterTitleTxt.setText("关于");
                    fragment = new aboutFragment();
                    break;
            }
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fl_achieve, fragment, "TAG_LEFT_SETTING");
            fragmentTransaction.commit();
        }

    }

    @Subscribe
    public void onEventMainThread(CondEvent event) {
        cond = event.getCond();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
