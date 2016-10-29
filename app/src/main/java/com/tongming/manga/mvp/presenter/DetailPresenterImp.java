package com.tongming.manga.mvp.presenter;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.modle.DetailModel;
import com.tongming.manga.mvp.view.activity.IDetailView;

/**
 * Created by Tongming on 2016/8/11.
 */
public class DetailPresenterImp extends BasePresenter implements IDetailPresenter, DetailModel.onGetDataListener {

    private IDetailView detailView;

    public DetailPresenterImp(IDetailView detailView) {
        this.detailView = detailView;
        baseModel = new DetailModel(this);
    }

    @Override
    public void getDetail(String source, String comicUrl) {
        detailView.showProgress();
        addSubscription(((DetailModel) baseModel).getDetail(source, comicUrl));
    }

    @Override
    public void addHistory(ComicInfo info, String historyName, String historyUrl) {
        ((DetailModel) baseModel).addHistory(info, historyName, historyUrl);
    }

    @Override
    public void updateHistory(ComicInfo info, String historyName, String historyUrl) {
        ((DetailModel) baseModel).updateHistory(info, historyName, historyUrl);
    }

    @Override
    public void queryHistoryByName(String name) {
        addSubscription(((DetailModel) baseModel).queryHistoryByName(name));
    }

    @Override
    public void collectComic(ComicInfo info) {
        ((DetailModel) baseModel).collectComic(info);
    }

    @Override
    public void queryCollectByName(String name) {
        ((DetailModel) baseModel).queryCollectByName(name);
    }

    @Override
    public void deleteCollectByName(String name) {
        ((DetailModel) baseModel).deleteCollectByName(name);
    }

    @Override
    public void collectComicOnNet(ComicInfo info) {
        addSubscription(((DetailModel) baseModel).collectComicOnNet(info));
    }

    @Override
    public void queryCollectOnNet(String name) {
        addSubscription(((DetailModel) baseModel).queryCollectOnNet(name));
    }

    @Override
    public void deleteCollectOnNet(ComicInfo info) {
        addSubscription(((DetailModel) baseModel).deleteCollectOnNet(info));
    }

    @Override
    public void onGetData(ComicInfo info) {
//        detailView.hideProgress();
        detailView.onGetData(info);
    }

    @Override
    public void onAddHistoryCompleted(long state) {
        detailView.onAddHistory(state);
    }

    @Override
    public void onUpdateHistoryCompleted(int state) {
        detailView.onUpdateHistory(state);
    }

    @Override
    public void onQueryHistoryCompleted(String historyName, String historyUrl) {
        detailView.onQueryHistory(historyName, historyUrl);
    }

    @Override
    public void onAddCollectCompleted(long state) {
        detailView.onAddCollect(state);
    }

    @Override
    public void onQueryCollectCompleted(boolean isCollected) {
        detailView.onQueryCollectByName(isCollected);
    }

    @Override
    public void onDeleteCollectCompleted(int state) {
        detailView.onDeleteCollectByName(state);
    }

    @Override
    public void onQueryCollectOnNet(boolean isCollected) {
        detailView.onQueryCollectOnNet(isCollected);
    }

    @Override
    public void onAddCollectOnNet(UserInfo info) {
        detailView.onAddCollectOnNet(info);
    }

    @Override
    public void onDeleteCollectOnNet(UserInfo info) {
        detailView.onDeleteCollectOnNet(info);
    }

    @Override
    public void onFail(Throwable throwable) {
        detailView.hideProgress();
        detailView.onFail();
        Logger.e(throwable.fillInStackTrace().toString());
    }
}
