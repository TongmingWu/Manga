package com.tongming.manga.mvp.view.activity;

/**
 * Created by Tongming on 2016/9/3.
 */
public interface ICacheView {
    void onClearCacheComplete();

    void onCalculateCache(long totalSize);

    void onFail(Throwable throwable);
}
