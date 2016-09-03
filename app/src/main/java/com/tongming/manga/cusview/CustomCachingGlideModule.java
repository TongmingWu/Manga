package com.tongming.manga.cusview;

import android.content.Context;
import android.content.SharedPreferences;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Created by Tongming on 2016/9/3.
 */
public class CustomCachingGlideModule implements GlideModule {

    public static final int DEFAULT_CACHE_CEILING = 1024 * 1024 * 200;

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        int cacheCeiling = sp.getInt("cacheCeiling", DEFAULT_CACHE_CEILING);
        if (cacheCeiling != DEFAULT_CACHE_CEILING) {
            cacheCeiling = cacheCeiling * 1024 * 1024;
        }
        File file = new File(context.getExternalCacheDir() + "/image_manager_disk_cache");
        file.mkdir();
        builder.setDiskCache(
                new ExternalCacheDiskCacheFactory(context, file.getName(), cacheCeiling));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
