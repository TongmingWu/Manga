package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tongming.manga.R;
import com.tongming.manga.mvp.view.activity.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/10.
 */
public class CategoryAdapter extends BaseAdapter {

    private List<Integer> picList;
    private List<String> nameList;
    private List<Integer> typeList;
    private Context mContext;

    public CategoryAdapter(List<Integer> picList, List<String> nameList, List<Integer> typeList, Context mContext) {
        this.picList = picList;
        this.nameList = nameList;
        this.typeList = typeList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public Object getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        /*Glide.with(mContext)
                .load(picList.get(position))
                .into(holder.ivCategory);*/
        holder.tvCategory.setText(nameList.get(position));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = typeList.get(position);
                int page = 1;
                int select = typeList.size() < 10 ? 0 : 1;
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("type", type)
                        .putExtra("page", page)
                        .putExtra("select", select)
                        .putExtra("name", nameList.get(position));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        View itemView;
        @BindView(R.id.item_root)
        LinearLayout root;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.iv_category)
        ImageView ivCategory;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
