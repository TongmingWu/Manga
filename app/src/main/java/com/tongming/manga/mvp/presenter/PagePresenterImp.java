package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.modle.IPageModel;
import com.tongming.manga.mvp.modle.PageModel;
import com.tongming.manga.mvp.view.activity.IPageView;

import java.util.List;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PagePresenterImp extends BasePresenter implements IPagePresenter, PageModel.onGetPageListener {

    private IPageView pageView;
    private IPageModel pageModel;

    public PagePresenterImp(IPageView pageView) {
        this.pageView = pageView;
        pageModel = new PageModel(this);
    }

    @Override
    public void getPage(String source, String chapterUrl) {
        pageView.showProgress();
        addSubscription(pageModel.getPage(source, chapterUrl));
    }

    @Override
    public void cacheImg(Context mContext, List<String> imgList, boolean isLast) {
        addSubscription(pageModel.cacheImg(mContext, imgList, isLast));
    }

    @Override
    public void onPageCompleted(List<String> imgList) {
        pageView.hideProgress();
        pageView.onPageCompleted(imgList);
    }

    @Override
    public void onSuccess(ComicPage page) {
        pageView.onSuccess(page);
    }

    @Override
    public void onFail(Throwable throwable) {
        pageView.hideProgress();
        pageView.onFail(throwable);
    }
}
