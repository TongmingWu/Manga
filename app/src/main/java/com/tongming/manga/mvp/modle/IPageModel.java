package com.tongming.manga.mvp.modle;

import android.content.Context;

import java.util.List;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IPageModel {
    Subscription getPage(String source, String chapterUrl);

    Subscription cacheImg(Context mContext, List<String> imgList, boolean isLast);
}
