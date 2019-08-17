package com.kaidongyuan.app.tyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.app.MyApplication;
import com.kaidongyuan.app.tyorder.bean.OrderTmsWay;
import com.kaidongyuan.app.tyorder.bean.TmsType;

import java.util.ArrayList;
import java.util.List;

public class OrderTmsWayAdapter extends BaseAdapter {

    private List<OrderTmsWay> mData;
    private Context mContext;

    public OrderTmsWayAdapter(List<OrderTmsWay> data, Context context) {
        this.mData = data==null? new ArrayList<OrderTmsWay>():data;
        this.mContext = context;
    }

    /**
     * 刷新 ListView
     * @param data 数据
     */
    public void notifyChange(List<OrderTmsWay> data) {
        this.mData = data==null? new ArrayList<OrderTmsWay>():data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public OrderTmsWay getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderTmsWayAdapter.ViewHolder holder;
        if (convertView==null) {
            holder = new OrderTmsWayAdapter.ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_business_listview, null);
            holder.tvBusinessType = (TextView) convertView.findViewById(R.id.textView_bussinessName);
            convertView.setTag(holder);
        } else {
            holder = (OrderTmsWayAdapter.ViewHolder) convertView.getTag();
        }

        String way = mData.get(position).getBUSINESS_IDX();
        if (TextUtils.isEmpty(way)) {
            way = MyApplication.getmRes().getString(R.string.no_set);
        }
        holder.tvBusinessType.setText(way);

        return convertView;
    }

    private class ViewHolder {
        TextView tvBusinessType;
    }
}
