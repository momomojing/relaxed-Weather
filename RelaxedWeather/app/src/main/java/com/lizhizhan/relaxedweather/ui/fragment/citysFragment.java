package com.lizhizhan.relaxedweather.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.DividerGridItemDecoration;
import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.adatper.myCitysAdapter;
import com.lizhizhan.relaxedweather.db.CurrentCityDao;
import com.lizhizhan.relaxedweather.ui.activity.AchieveActivity;
import com.lizhizhan.relaxedweather.ui.view.LoadingPage;
import com.lizhizhan.relaxedweather.utils.UIUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 已经添加城市
 * Created by lizhizhan on 2017/1/15.
 */

public class citysFragment extends baseFragment {
    @InjectView(R.id.citys_recy)
    RecyclerView citysRecy;
    @InjectView(R.id.bt_enter_add)
    TextView btEnterAdd;
    private ArrayList<String> mycitys;

    @Override
    public View OnCreatSuccessView() {
        View view = UIUtils.inflate(R.layout.citys_fragment);
        ButterKnife.inject(this, view);


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
        return view;
    }

    @Override
    public LoadingPage.ResultState onLoad() {

        mycitys = getMycity();

        return LoadingPage.ResultState.STATE_SUCCESS;
    }


    private ArrayList<String> getMycity() {
        CurrentCityDao currentCityDao = new CurrentCityDao(context);
        ArrayList<String> allCitys = currentCityDao.findAll();
        return allCitys;

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
