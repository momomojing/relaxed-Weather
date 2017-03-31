package com.lizhizhan.relaxedweather.ui.fragment;

import android.view.View;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.ui.view.LoadingPage;
import com.lizhizhan.relaxedweather.utils.UIUtils;

/**
 * 添加城市
 * Created by lizhizhan on 2017/1/15.
 */

public class aboutFragment extends baseFragment {
    @Override
    public View OnCreatSuccessView() {
        View view = UIUtils.inflate(R.layout.about_fragment);
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

}
