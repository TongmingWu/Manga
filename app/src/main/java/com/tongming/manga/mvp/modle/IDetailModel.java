package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.ComicInfo;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IDetailModel {
    Subscription getDetail(String comicUrl);

    Subscription addHistory(ComicInfo info, String historyName, String historyUrl);

    Subscription queryHistoryByName(String comicName);

    Subscription updateHistory(ComicInfo info, String historyName, String historyUrl);

    Subscription collectComic(ComicInfo info);

    Subscription queryCollectByName(String name);

    Subscription deleteCollectByName(String name);
}
