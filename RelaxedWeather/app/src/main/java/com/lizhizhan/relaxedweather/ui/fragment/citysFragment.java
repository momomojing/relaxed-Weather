package com.lizhizhan.relaxedweather.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.DividerGridItemDecoration;
import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.adatper.myCitysAdapter;
import com.lizhizhan.relaxedweather.db.CurrentCityDao;
import com.lizhizhan.relaxedweather.ui.activity.AchieveActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 添加城市
 * Created by lizhizhan on 2017/1/15.
 */

public class citysFragment extends baseFragment {
    @InjectView(R.id.citys_recy)
    RecyclerView citysRecy;
    @InjectView(R.id.bt_enter_add)
    TextView btEnterAdd;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void lazyLoad() {
    }


    private ArrayList<String> getMycity() {
        CurrentCityDao currentCityDao = new CurrentCityDao(context);
        ArrayList<String> allCitys = currentCityDao.findAll();
        return allCitys;

    }

    @Override
    public Integer getRealViewID() {
        return R.layout.citys_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);

        ArrayList<String> mycitys = getMycity();

        if (mycitys.size() == 0) {
            btEnterAdd.setVisibility(View.VISIBLE);
            citysRecy.setVisibility(View.GONE);
        } else {
            btEnterAdd.setVisibility(View.GONE);
            citysRecy.setVisibility(View.VISIBLE);
        }

        citysRecy.setLayoutManager(new GridLayoutManager(context, 3));
        myCitysAdapter adapter = new myCitysAdapter(context, mycitys);

        citysRecy.addItemDecoration(new DividerGridItemDecoration(
                getActivity()));

        citysRecy.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.bt_enter_add)
    public void onClick() {
        Intent intent = new Intent(context, AchieveActivity.class);
        //0 是添加
        intent.putExtra("category", 0);
        startActivity(intent);
    }
}
