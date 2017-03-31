package com.lizhizhan.relaxedweather.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lizhizhan.relaxedweather.ui.view.LoadingPage;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.UIUtils;

/**
 * Created by lizhizhan on 2017/1/13.
 */

public abstract class baseFragment extends Fragment {
    //控件是否已经初始化
    private boolean isCreateView = false;
    //是否已经加载过数据
    private boolean isLoadData = false;
    protected Context context;
    private LoadingPage loadingPage;

    protected abstract View OnCreatSuccessView();

    protected abstract LoadingPage.ResultState onLoad();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        loadingPage = new LoadingPage(UIUtils.getContext()) {
            @Override
            public View onCreatSuccessView() {
                return baseFragment.this.OnCreatSuccessView();
            }

            @Override
            public ResultState initData() {
                return onLoad();
            }
        };
        //        ViewGroup parent = (ViewGroup) loadingPage.getParent();
        //        if (parent != null) {
        //            parent.removeView(loadingPage);
        //        }
        isCreateView = true;
        Logger.e("onCreateView" + getClass().getName());
        return loadingPage;
    }


    //第一个之后的Fragment 会从这里加载数据
    //在oncreateView  ,onViewCreated后面
        @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && !isCreateView) {
                //如果没有加载过就加载，否则就不再加载了
                if (!isLoadData) {
                    lazyLoad();
                    isLoadData = true;
                }
            }
        }

    /**
     * 在onCreateView，onViewCreated 后面
     *
     * @param savedInstanceState
     */
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            //第一个fragment会调用.
            if (getUserVisibleHint()) {
                lazyLoad();
            }
        }

    /**
     * 懒加载数据，跟新UI
     */
    public void lazyLoad() {
        if (loadingPage != null) {
            loadingPage.LoadData();
        }
    }
}
