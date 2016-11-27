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

/**
 * Author: Tongming
 * Date: 2016/9/20
 */

public class HeaderGlide {

    public static final String URL_DMZJ = "http://m.dmzj.com/";
    public static final String URL_IKAN = "http://m.ikanman.com/";
    public static final String HOST_IKAN = "p.yogajx.com";
    public static final String REFERER = "Referer";
    public static final String HOST = "Host";

    private static String getReferer(String source) {
        String referer_url = URL_DMZJ;
        if (source.equals(ApiManager.SOURCE_DMZJ)) {
            referer_url = URL_DMZJ;
        } else if (source.equals(ApiManager.SOURCE_IKAN)) {
            referer_url = URL_IKAN;
        }
        return referer_url;
    }

    public static void loadImage(Context context, String url, ImageView view, String source) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, getReferer(source))
                .build());
        Glide.with(context)
                .load(glideUrl)
//                .placeholder(R.drawable.comic_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(view);
    }

    public static void loadBitmap(Context context, String url, final ImageView view, String source) {
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader(REFERER, getReferer(source))
                .build());
        Glide.with(context)
                .load(glideUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        view.setImageBitmap(FastBlur.doBlur(resource, 100, false));
                    }
                });
    }

    public static void downloadImage(Context context, String url, String source) {
        String referer = getReferer(source);
        LazyHeaders.Builder builder = new LazyHeaders.Builder();
        builder.addHeader(REFERER, referer);
        if (referer.equals(URL_IKAN)) {
            builder.addHeader(HOST, HOST_IKAN);
        }
        GlideUrl glideUrl = new GlideUrl(url, builder.build());
        Glide.with(context)
                .load(glideUrl)
                .downloadOnly(720, 1280);
    }
}
