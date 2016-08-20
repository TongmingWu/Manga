package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.tongming.manga.mvp.bean.ComicInfo;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IDetailPresenter {
    void getDetail(String comicUrl);

    void addHistory(Context context, ComicInfo info, String historyName, String historyUrl);

    void queryHistoryByName(Context context, String name);

    void updateHistory(Context context, ComicInfo info, String historyName, String historyUrl);

    void collectComic(Context context, ComicInfo info);

    void queryCollectByName(Context context, String name);

    void deleteCollectByName(Context context,String name);
}
