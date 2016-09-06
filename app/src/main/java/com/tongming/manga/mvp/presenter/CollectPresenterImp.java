package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.modle.CollectModel;
import com.tongming.manga.mvp.modle.ICollectModel;
import com.tongming.manga.mvp.view.activity.ICollectView;

import java.util.List;

/**
 * Created by Tongming on 2016/8/17.
 */
public class CollectPresenterImp extends BasePresenter implements ICollectPresenter, CollectModel.OnCollectListener {

    private ICollectModel collectModel;
    private ICollectView collectView;

    public CollectPresenterImp(ICollectView collectView) {
        this.collectView = collectView;
        collectModel = new CollectModel(this);
    }

    @Override
    public void queryAllCollect(Context context) {
        addSubscription(collectModel.queryAllCollect(context));
    }

    @Override
    public void deleteCollectByName(Context context, String name) {
        collectModel.deleteCollectByName(context, name);
    }

    @Override
    public void deleteAllCollect(Context context) {
        collectModel.deleteAllCollect(context);
    }

    @Override
    public void onQueryAllCompleted(List<CollectedComic> comics) {
        collectView.onQueryAllCollect(comics);
    }

    @Override
    public void deleteCollectOnNet(String name) {
        addSubscription(collectModel.deleteCollectOnNet(name));
    }

    @Override
    public void onDeleteByName(int state) {
        collectView.onDeleteCollectByName(state);
    }

    @Override
    public void onDeleteAll(int state) {
        collectView.onDeleteAllCollect(state);
    }

    @Override
    public void onDeleteOnNet(UserInfo info) {
        collectView.onDeleteCollectOnNet(info);
    }

    @Override
    public void onFail(Throwable throwable) {
        collectView.onFail(throwable);
    }
}
