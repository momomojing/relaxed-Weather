package com.lizhizhan.relaxedweather.ui.fragment;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.ui.view.AddHolder;
import com.lizhizhan.relaxedweather.ui.view.LoadingPage;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 添加城市
 * Created by lizhizhan on 2017/1/15.
 */

public class AddCityFragment extends baseFragment {
    @InjectView(R.id.searchView)
    SearchView searchView;
    @InjectView(R.id.fl_hot)
    FrameLayout flHot;

    private AddHolder holder;

    @Override
    public View OnCreatSuccessView() {
        holder = new AddHolder(context);
        holder.refreshView(null);
        View rootView = holder.getmRootView();
        return rootView;
    }

    @Override
    public LoadingPage.ResultState onLoad() {
        return LoadingPage.ResultState.STATE_SUCCESS;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
