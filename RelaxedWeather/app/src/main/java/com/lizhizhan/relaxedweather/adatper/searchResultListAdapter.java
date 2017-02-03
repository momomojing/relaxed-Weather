package com.lizhizhan.relaxedweather.adatper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.bean.CityInfos;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lizhizhan on 2017/1/15.
 */

public class searchResultListAdapter extends BaseAdapter {

    private List<CityInfos> all;

    public void setData(List<CityInfos> all) {
        this.all = all;
    }

    @Override
    public int getCount() {
        return all.size();
    }

    @Override
    public Object getItem(int position) {
        return all.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_city, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.refreshView(all.get(position));
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_city_name)
        TextView tvCityName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void refreshView(CityInfos cityInfos) {
            tvCityName.setText(cityInfos.getCity());
        }
    }
}
