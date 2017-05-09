package com.tongming.manga.mvp.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.tongming.manga.R;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.util.CommonUtil;
import com.tongming.manga.util.HeaderGlide;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageHolder> {

    private List<String> picList;
    private File[] fileList;
    private Context mContext;
    private int height;
    private int width;
    private String SOURCE_URL;
    public static final int NET_IMAGE_FLAG = 0xff;
    public static final int LOCAL_IMAGE_FLAG = 0xee;

    public PageAdapter(List<String> picList, Context mContext, String source) {
        this.picList = picList;
        this.mContext = mContext;
        if (source.equals(ApiManager.SOURCE_DMZJ)) {
            SOURCE_URL = HeaderGlide.URL_DMZJ;
        } else if (source.equals(ApiManager.SOURCE_IKAN)) {
            SOURCE_URL = HeaderGlide.URL_IKAN;
        }
        getWH();
    }

    private void getWH() {
        height = CommonUtil.getDeviceHeight((Activity) mContext);
        width = CommonUtil.getDeviceWidth((Activity) mContext);
    }

    @Override
    public PageAdapter.PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_page, null);
        return new PageHolder(view);
    }

    @Override
    public void onBindViewHolder(final PageAdapter.PageHolder holder, final int position) {
//         scaleImageView(1.78f, holder);
        Object temp = null;

        String url = picList.get(position);
        if (url.contains("http")) {
            temp = new GlideUrl(url, new LazyHeaders.Builder()
                    .addHeader(HeaderGlide.REFERER, SOURCE_URL)
                    .build());
        } else {
            temp = new File(url);
        }
        Glide.with(mContext)
                .load(temp)
                .dontAnimate()
                .placeholder(R.color.gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
//                 .bitmapTransform(new BitmapTransformation(mContext) {
//                     @Override
//                     protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//                         float scale = (float) toTransform.getHeight() / (float) toTransform.getWidth();
//                         scaleImageView(scale, holder);
//                         return toTransform;
//                     }

//                     @Override
//                     public String getId() {
//                         return PageAdapter.class.getSimpleName();
//                     }
//                 })
                .into(holder.ivPage);
    }

    @Override
    public int getItemCount() {
        return picList.size();
    }

    /**
     * 卷轴模式进行缩放,左右模式不缩放,并使用fitCenter
     */
    private void scaleImageView(float scale, PageAdapter.PageHolder holder) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.ivPage.getLayoutParams();
        params.height = (int) (width * scale);
        params.width = width;
    }

    class PageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_page)
        ImageView ivPage;

        PageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
