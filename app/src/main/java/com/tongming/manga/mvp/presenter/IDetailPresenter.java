package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.bean.ComicInfo;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IDetailPresenter {
    void getDetail(String comicUrl);

    void addHistory(ComicInfo info, String historyName, String historyUrl);

    void queryHistoryByName(String name);

    void updateHistory(ComicInfo info, String historyName, String historyUrl);

    void collectComic(ComicInfo info);

    void queryCollectByName(String name);

    void deleteCollectByName(String name);
}
