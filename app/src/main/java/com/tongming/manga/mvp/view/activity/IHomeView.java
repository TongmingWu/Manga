package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.Hot;

/**
 * Created by Tongming on 2016/8/9.
 */
public interface IHomeView{
    void onLoad(Hot hot);
    void onFail();
    void showRefresh();
    void hideRefresh();
}
