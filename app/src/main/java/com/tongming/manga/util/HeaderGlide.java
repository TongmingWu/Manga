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

/**
 * Author: Tongming
 * Date: 2016/9/20
 */

public class HeaderGlide {

    private static final String URL = "http://m.dmzj.com/";
    private static final String REFERER = "Referer";

    public static void loadImage(Context context, String url, ImageView view) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, URL)
                .build());
        Glide.with(context)
                .load(glideUrl)
                .crossFade()
                .into(view);
    }

    public static void loadBitmap(Context context, String url, final ImageView view) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, URL)
                .build());
        Glide.with(context)
                .load(glideUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        view.setImageBitmap(FastBlur.doBlur(resource, 5, false));
                    }
                });
    }

    public static void loadPage(Context context, String url, ImageView view) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, URL)
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
                .addHeader(REFERER, URL)
                .build());
        Glide.with(context)
                .load(glideUrl)
                .downloadOnly(720, 1280);
    }
}
