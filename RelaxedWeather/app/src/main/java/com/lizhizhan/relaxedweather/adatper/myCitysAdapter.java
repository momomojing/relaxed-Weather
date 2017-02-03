package com.lizhizhan.relaxedweather.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lizhizhan.relaxedweather.R;
import com.lizhizhan.relaxedweather.bean.CondEvent;
import com.lizhizhan.relaxedweather.db.CurrentCityDao;
import com.lizhizhan.relaxedweather.utils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lizhizhan on 2017/1/26.
 */

public class myCitysAdapter extends ListAdapter<String> {

    public myCitysAdapter(Context context, ArrayList<String> mDatas) {
        super(context, mDatas);
    }

    @Override
    protected RecyclerView.ViewHolder getRealViewHolder(View view) {
        return new cityHolder(view);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.city_item;
    }

    @Override
    protected void refreshUi(final RecyclerView.ViewHolder holder, int position) {
        final cityHolder cityHolder = (cityHolder) holder;
        // 如果设置了回调，则设置点击事件
        //        if (mOnItemClickLitener != null) {
        //            holder.itemView.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View v) {
        //                    int pos = cityHolder.getLayoutPosition();
        //                    mOnItemClickLitener.onItemClick(cityHolder.itemView, pos);
        //                }
        //            });
        //            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        //                @Override
        //                public boolean onLongClick(View v) {
        //                    int pos = cityHolder.getLayoutPosition();
        //                    mOnItemClickLitener.onItemLongClick(cityHolder.itemView, pos);
        //                    return false;
        //                }
        //            });
        //        }

        String s = mDatas.get(position);
        cityHolder.refreshUI(s, position);

    }

    class cityHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.tv_city)
        TextView tvCity;

        cityHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        void refreshUI(final String s, final int position) {
            tvCity.setText(s);
            tvCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeData(position);
                }
            });
        }
    }

    public void addData(int position) {
        mDatas.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        int oldSize = mDatas.size();
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDatas.size() - position);
        notifyDataSetChanged();
        String s = mDatas.get(position);
        mDatas.remove(position);
        int newSize = mDatas.size();
        if (oldSize != newSize) {
            //条目发生变化，发送通知
            EventBus.getDefault().post(new CondEvent(true));
        }

        CurrentCityDao dao = new CurrentCityDao(UIUtils.getContext());
        dao.delete(s);

    }
}
