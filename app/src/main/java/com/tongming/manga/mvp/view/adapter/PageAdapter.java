package com.tongming.manga.mvp.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tongming.manga.R;
import com.tongming.manga.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements onCalculateListener {

    private List<String> picList;
    private Context mContext;
    private int height;
    private int width;

    public PageAdapter(List<String> picList, Context mContext) {
        this.picList = picList;
        this.mContext = mContext;
        height = CommonUtil.getDeviceHeight(mContext);
        width = CommonUtil.getDeviceWidth(mContext);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_page, null);
        PageHolder holder = new PageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        /*Glide.with(mContext)
                .load(picList.get(position))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //确保从缓存中获取图片
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        float scale = (float) resource.getHeight() / (float) resource.getWidth();
                        PageAdapter.this.onCalculateComplete(scale, position, holder);
                    }
                });*/
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((PageHolder) holder).ivPage.getLayoutParams();
        if (height < width) {
            params.height = (int) (width * 1.6);
        } else {
            params.height = height;
        }
        params.width = width;
        Glide.with(mContext)
                .load(picList.get(position))
                .crossFade()
                .placeholder(R.drawable.bg_page)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(((PageHolder) holder).ivPage);
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    @Override
    public void onCalculateComplete(float scale, int position, RecyclerView.ViewHolder holder) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ((PageHolder) holder).ivPage.getLayoutParams();
        if (height < width) {
            params.height = (int) (width * scale);
            ((PageHolder) holder).ivPage.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            params.height = height;
            ((PageHolder) holder).ivPage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        params.width = width;
        Glide.with(mContext)
                .load(picList.get(position))
                .centerCrop()
                .placeholder(R.drawable.bg_page)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((PageHolder) holder).ivPage);

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
