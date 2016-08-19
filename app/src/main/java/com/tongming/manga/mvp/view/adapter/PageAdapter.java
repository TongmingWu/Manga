package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tongming.manga.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> picList;
    private Context mContext;

    public PageAdapter(List<String> picList, Context mContext) {
        this.picList = picList;
        this.mContext = mContext;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_page, null);
        PageHolder holder = new PageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Glide.with(mContext)
                .load(picList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //确保从缓存中获取图片
                .placeholder(R.drawable.bg_page)
                .fitCenter()
                .into(((PageHolder) holder).ivPage);

    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    class PageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_page)
        ImageView ivPage;

        public PageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
