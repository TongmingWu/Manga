package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.modle.ISystemModel;
import com.tongming.manga.mvp.modle.SystemModel;
import com.tongming.manga.mvp.view.activity.ISystemView;

/**
 * Created by Tongming on 2016/8/14.
 */
public class SystemPresenterImp extends BasePresenter implements ISystemPresenter, SystemModel.onCompleteListener {

    private ISystemView systemView;
    private ISystemModel systemModel;

    public SystemPresenterImp(ISystemView systemView) {
        this.systemView = systemView;
        systemModel = new SystemModel(this);
    }

    @Override
    public void clearCache(Context context, boolean clearAll) {
        addSubscription(systemModel.clearCache(context, clearAll));
    }

    @Override
    public void calculateCacheSize(Context context) {
        addSubscription(systemModel.calculateSize(context));
    }

    @Override
    public void onClearCacheCompleted() {
        systemView.onClearCache();
    }

    @Override
    public void onCalculateCompleted(long size) {
        systemView.onCalculateCacheSize(size);
    }

    @Override
    public void onFail() {

    }
}
