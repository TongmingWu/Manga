package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.modle.CacheModel;
import com.tongming.manga.mvp.modle.ICacheModel;
import com.tongming.manga.mvp.view.activity.ICacheView;

/**
 * Created by Tongming on 2016/9/3.
 */
public class CachePresenterImp extends BasePresenter implements ICachePresenter, CacheModel.onCacheListener {

    private ICacheModel cacheModel;
    private ICacheView cacheView;

    public CachePresenterImp(ICacheView cacheView) {
        this.cacheView = cacheView;
        cacheModel = new CacheModel(this);
    }

    @Override
    public void clearCache() {
        addSubscription(cacheModel.clearCache());
    }

    @Override
    public void calculateCacheSize() {
        addSubscription(cacheModel.calculateSize());
    }

    @Override
    public void onClearCacheComplete() {
        cacheView.onClearCacheComplete();
    }

    @Override
    public void onCalculateComplete(long totalSize) {
        cacheView.onCalculateCache(totalSize);
    }

    @Override
    public void onFail(Throwable e) {
        cacheView.onFail(e);
    }
}
