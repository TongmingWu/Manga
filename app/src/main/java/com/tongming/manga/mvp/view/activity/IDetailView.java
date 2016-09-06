package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.UserInfo;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IDetailView {
    void onGetData(ComicInfo info);

    void onQueryHistory(String historyName, String historyUrl);

    void onAddHistory(long state);

    void onUpdateHistory(int state);

    void onQueryCollectByName(boolean isCollected);

    void onAddCollect(long state);

    void onDeleteCollectByName(int state);

    void onQueryCollectOnNet(boolean isCollected);

    void onAddCollectOnNet(UserInfo info);

    void onDeleteCollectOnNet(UserInfo info);

    void onFail();

    void showProgress();

    void hideProgress();
}
