package com.tongming.manga.mvp.presenter;

import android.content.Context;

/**
 * Created by Tongming on 2016/8/17.
 */
public interface ICollectPresenter {
    void queryAllCollect(Context context);

    void deleteCollectByName(Context context,String name);

    void deleteAllCollect(Context context);

    void deleteCollectOnNet(String name);
}
