package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.UserInfo;

import java.util.List;

/**
 * Created by Tongming on 2016/8/17.
 */
public interface ICollectView {
    void onQueryAllCollect(List<CollectedComic> comics);

    void onDeleteCollectByName(int state);

    void onDeleteAllCollect(int state);

    void onDeleteCollectOnNet(UserInfo info);

    void onFail(Throwable throwable);
}
