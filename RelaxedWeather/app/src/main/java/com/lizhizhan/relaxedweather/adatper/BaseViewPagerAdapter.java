package com.lizhizhan.relaxedweather.adatper;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2017/1/11.
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final ArrayList<FragmentInfo> fragments;

    public BaseViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        fragments = new ArrayList<FragmentInfo>();
    }

    public void addPager(Class<?> clazz, Bundle bundle) {
        fragments.add(new FragmentInfo(clazz, bundle));
    }

    @Override
    public Fragment getItem(int position) {
        FragmentInfo fragmentInfo = fragments.get(position);
        return Fragment.instantiate(context, fragmentInfo.getClazz().getName(), fragmentInfo.getBundle());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public class FragmentInfo {

        private final Class<?> clazz;
        private final Bundle bundle;

        public FragmentInfo(Class<?> clazz, Bundle bundle) {
            this.clazz = clazz;
            this.bundle = bundle;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public Bundle getBundle() {
            return bundle;
        }
    }
}
