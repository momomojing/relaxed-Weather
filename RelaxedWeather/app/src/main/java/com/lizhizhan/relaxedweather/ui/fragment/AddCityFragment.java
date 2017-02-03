package com.lizhizhan.relaxedweather.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.golbal.Constant;
import com.lizhizhan.relaxedweather.MainActivity;
import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.adatper.searchResultListAdapter;
import com.lizhizhan.relaxedweather.bean.CityInfos;
import com.lizhizhan.relaxedweather.db.CityDao;
import com.lizhizhan.relaxedweather.db.CurrentCityDao;
import com.lizhizhan.relaxedweather.ui.view.FlowLayout;
import com.lizhizhan.relaxedweather.utils.DrawableUtils;
import com.lizhizhan.relaxedweather.utils.Logger;
import com.lizhizhan.relaxedweather.utils.StringUtils;
import com.lizhizhan.relaxedweather.utils.ToastUtil;
import com.lizhizhan.relaxedweather.utils.UIUtils;

import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 添加城市
 * Created by lizhizhan on 2017/1/15.
 */

public class AddCityFragment extends baseFragment implements AdapterView.OnItemClickListener {
    @InjectView(R.id.searchView)
    SearchView searchView;
    @InjectView(R.id.fl_hot)
    FrameLayout flHot;
    private PopupWindow popupWindow;
    private WindowManager systemService;
    private List<CityInfos> all;

    @Override
    protected void lazyLoad() {
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        textView.setTextColor(Color.BLACK);
        textView.setHintTextColor(Color.GRAY);
        int i = 0;
        initHotCity();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索(回车)按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                Logger.e("onQueryTextSubmit" + query);
                if (!StringUtils.isEmpty(query)) {
                    CityDao cityDao = new CityDao();
                    all = cityDao.findAll(query);
                    if (all != null) {
                        String province = all.get(0).getProvince();
                        Logger.e("a" + all.size() + province);
                        showSeachResult(all);
                    }
                }
                //                searchView.setIconified(true);
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                Logger.e("onQueryTextChange" + newText);
                if (!StringUtils.isEmpty(newText)) {
                    CityDao cityDao = new CityDao();
                    all = cityDao.findAll(newText);
                    if (all.size() != 0) {
                        String province = all.get(0).getProvince();
                        Logger.e("a" + all.size() + province);
                        showSeachResult(all);
                    }
                }


                return false;
            }


        });
    }

    private void initHotCity() {
        //        可以上下滑动的
        ScrollView scrollView = new ScrollView(UIUtils.getContext());
        FlowLayout flow = new FlowLayout(UIUtils.getContext());
        int padding = UIUtils.dip2px(20);
        int paddingTB = UIUtils.dip2px(0);
        flow.setPadding(padding, paddingTB, padding, paddingTB);
        flow.setHorizontalSpacing(UIUtils.dip2px(50));//水平布局
        flow.setVerticalSpacing(UIUtils.dip2px(30));//竖直布局
        int length = Constant.HOTCITYS.length;
        for (int i = 0; i < length; i++) {
            final String value = Constant.HOTCITYS[i];
            TextView textView = new TextView(UIUtils.getContext());
            textView.setText(value);
            textView.setTextColor(Color.BLACK);
            //大小
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setGravity(Gravity.CENTER);
            Random random = new Random();
            //随机颜色
            //r g b .0 -255
            //            int r = 30 + random.nextInt(200);
            //            int g = 30 + random.nextInt(200);
            //            int b = 30 + random.nextInt(200);
            int r = 200;
            int g = 200;
            int b = 200;
            int color = 0xffcecece;//摁下的颜色

            StateListDrawable selector = DrawableUtils.getSelector(Color.rgb(r, g, b), color, UIUtils.dip2px(6));

            textView.setBackground(selector);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showToast(value);
                    addViewPager(value);
                }
            });
            flow.addView(textView);
        }
        scrollView.addView(flow);
        flHot.addView(scrollView);

    }

    /**
     * 添加新的ViewPager
     *
     * @param city
     */
    private void addViewPager(String city) {
        CurrentCityDao currentCityDao = new CurrentCityDao(context);
        currentCityDao.add(city);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("addNewCity", city);
        startActivity(intent);
    }


    @Override
    public Integer getRealViewID() {
        return R.layout.addcity_fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    private void showSeachResult(List<CityInfos> all) {
        ListView listView = new ListView(context);
        listView.setDividerHeight(1);
        //        listView.setBackgroundResource(R.drawable.listview_background);
        listView.setOnItemClickListener(this);
        searchResultListAdapter adapter = new searchResultListAdapter();
        adapter.setData(all);

        listView.setAdapter(adapter);
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //获取到屏幕宽高
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        // 显示下拉选择框
        popupWindow = new PopupWindow(listView, width / 2, height / 3);

        // 设置点击外部区域, 自动隐藏
        popupWindow.setOutsideTouchable(true); // 外部可触摸
        //        popupWindow.setBackgroundDrawable(new BitmapDrawable()); // 设置空的背景, 响应点击事件
        popupWindow.setFocusable(false); //设置可获取焦点
        // 显示在指定控件下
        popupWindow.showAsDropDown(searchView, width / 4, 5);
    }

    /**
     * listView点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ToastUtil.showToast(all.get(position).getCity());
        if (all != null) {
            Logger.e("点击了； " + all.get(position).getCity());
            addViewPager(all.get(position).getCity());
        }
    }
}
