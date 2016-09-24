package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.ComicPage;

import java.util.List;

/**
 * Created by Tongming on 2016/8/11.
 */
public interface IPageView {
    void onSuccess(ComicPage page);
    void onFail(Throwable throwable);
    void showProgress();
    void hideProgress();
    void onPageCompleted(List<String> imgList);
}
