package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tongming.manga.R;
import com.tongming.manga.mvp.bean.SearchRecord;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/10/10
 */

public class SearchAdapter extends BaseAdapter {

    private List<SearchRecord> recordList;
    private Context mContext;

    public SearchAdapter(List<SearchRecord> recordList, Context mContext) {
        this.recordList = recordList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return recordList.size() > 5 ? 5 : recordList.size();
    }

    @Override
    public Object getItem(int position) {
        return recordList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(mContext, R.layout.item_hint, null);
        TextView view = (TextView) convertView.findViewById(R.id.tv_search);
        view.setText(recordList.get(position).getComic_name());
        return convertView;
    }
}
