package com.tongming.manga.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tongming.manga.mvp.api.ApiManager;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Author: Tongming
 * Date: 2016/9/20
 */

public class HeaderGlide {

    public static final String URL_DMZJ = "http://m.dmzj.com/";
    public static final String URL_IKAN = "http://m.ikanman.com/";
    public static final String REFERER = "Referer";

    private static String getReferer() {
        String referer_url = URL_DMZJ;
        String source = context.getSharedPreferences("config", Context.MODE_PRIVATE).getString("source", ApiManager.SOURCE_DMZJ);
        if (source.equals(ApiManager.SOURCE_DMZJ)) {
            referer_url = URL_DMZJ;
        } else if (source.equals(ApiManager.SOURCE_IKAN)) {
            referer_url = URL_IKAN;
        }
        return referer_url;
    }

    public static void loadImage(Context context, String url, ImageView view) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, getReferer())
                .build());
        Glide.with(context)
                .load(glideUrl)
//                .placeholder(R.drawable.comic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(view);
    }

    public static void loadBitmap(Context context, String url, final ImageView view) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, getReferer())
                .build());
        Glide.with(context)
                .load(glideUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        view.setImageBitmap(FastBlur.doBlur(resource, 15, false));
                    }
                });
    }

    public static void loadPage(Context context, String url, ImageView view) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, getReferer())
                .build());
        Glide.with(context)
                .load(glideUrl)
                .dontAnimate()
//                .placeholder(R.color.gray)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(view);
    }

    public static void downloadImage(Context context, String url) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, getReferer())
                .build());
        Glide.with(context)
                .load(glideUrl)
                .downloadOnly(720, 1280);
    }
}
