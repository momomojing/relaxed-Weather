package com.lizhizhan.relaxedweather.adatper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by lizhizhan on 2016/12/2.
 */

public abstract class ListAdapter<T> extends RecyclerView.Adapter {

    public ArrayList<T> mDatas;
    public final Context context;

    public ListAdapter(Context context, ArrayList<T> mDatas) {
        this.mDatas = mDatas;
        this.context = context;
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, getItemLayoutId(), null);
        return getRealViewHolder(view);
    }

    protected abstract RecyclerView.ViewHolder getRealViewHolder(View view);

    public abstract int getItemLayoutId();

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        refreshUi(holder, position);
    }

    protected abstract void refreshUi(RecyclerView.ViewHolder holder, int position);

}
