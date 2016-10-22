package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.ComicInfo;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IDetailModel {
    Subscription getDetail(String source, String comicUrl);

    void addHistory(ComicInfo info, String historyName, String historyUrl);

    Subscription queryHistoryByName(String comicName);

    void updateHistory(ComicInfo info, String historyName, String historyUrl);

    void collectComic(ComicInfo info);

    Subscription queryCollectByName(String name);

    void deleteCollectByName(String name);

    Subscription collectComicOnNet(ComicInfo info);

    Subscription queryCollectOnNet(String name);

    Subscription deleteCollectOnNet(ComicInfo info);
}
