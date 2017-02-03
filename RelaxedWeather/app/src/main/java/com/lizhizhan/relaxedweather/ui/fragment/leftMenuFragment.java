package com.lizhizhan.relaxedweather.ui.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.adatper.leftMenuAdapter;
import com.lizhizhan.relaxedweather.ui.activity.AchieveActivity;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by lizhizhan on 2017/1/13.
 */

public class leftMenuFragment extends baseFragment {
    @InjectView(R.id.tv_text)
    TextView tvText;
    String te = "出生";
    @InjectView(R.id.ll_left_menu)
    LinearLayout llLeftMenu;
    @InjectView(R.id.lv_left)
    ListView lvLeft;
    private String[] items = new String[]{"添加", "地点", "设置", "关于"};


    @Override
    public Integer getRealViewID() {
        return R.layout.left_menu;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void lazyLoad() {
        Logger.e("调用" + te);
        //打开时，获取焦点，屏蔽主面板的刷新
        llLeftMenu.setFocusable(true);
        leftMenuAdapter leftMenuAdapter = new leftMenuAdapter();

        for (int i = 0; i < items.length; i++) {
            leftMenuAdapter.addItemNames(items[i]);
        }

        lvLeft.setAdapter(leftMenuAdapter);

        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAchieveActivity(position);
            }
        });
    }


    /**
     * 跳转到具体的实现页面
     *
     * @param position
     */
    private void startAchieveActivity(int position) {

        Intent intent = new Intent(context, AchieveActivity.class);
        intent.putExtra("category", position);
        startActivity(intent);
        switch (position) {
            case 0:
                ToastUtil.showToast("添加");
                break;
            case 1:
                ToastUtil.showToast("设置");
                break;
            case 2:
                ToastUtil.showToast("关于");
                break;
        }
    }
}
