package com.lizhizhan.relaxedweather.adatper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.utils.UIUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lizhizhan on 2017/1/14.
 */

public class leftMenuAdapter extends BaseAdapter {
    private ArrayList<String> items;
    private ArrayList<Integer> itemsSrcId;

    public leftMenuAdapter() {
        items = new ArrayList<>();
        itemsSrcId = new ArrayList<>();
        itemsSrcId.add(R.mipmap.add);
        itemsSrcId.add(R.mipmap.setting);
        itemsSrcId.add(R.mipmap.about);
        itemsSrcId.add(R.mipmap.about);
    }

    public void addItemNames(String name) {
        items.add(name);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = UIUtils.inflate(R.layout.left_menu_item);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.refreshView(items.get(position), itemsSrcId.get(position));

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.iv1)
        ImageView iv1;
        @InjectView(R.id.tv_item_name)
        TextView tvItemName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }

        public void refreshView(String name, Integer mipmapID) {
            tvItemName.setText(name);
            iv1.setImageResource(mipmapID);
        }
    }
}
