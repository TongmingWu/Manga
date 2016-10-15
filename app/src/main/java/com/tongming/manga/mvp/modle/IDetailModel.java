package com.tongming.manga.mvp.modle;

import android.content.Context;

import com.tongming.manga.mvp.bean.ComicInfo;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IDetailModel {
    Subscription getDetail(String source, String comicUrl);

    void addHistory(Context context, ComicInfo info, String historyName, String historyUrl);

    Subscription queryHistoryByName(Context context, String comicName);

    void updateHistory(Context context, ComicInfo info, String historyName, String historyUrl);

    void collectComic(Context context, ComicInfo info);

    Subscription queryCollectByName(Context context, String name);

    void deleteCollectByName(Context context, String name);

    Subscription collectComicOnNet(ComicInfo info);

    Subscription queryCollectOnNet(String name);

    Subscription deleteCollectOnNet(ComicInfo info);
}
