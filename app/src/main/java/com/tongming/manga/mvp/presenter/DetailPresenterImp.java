package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.modle.DetailModel;
import com.tongming.manga.mvp.modle.IDetailModel;
import com.tongming.manga.mvp.view.activity.IDetailView;

/**
 * Created by Tongming on 2016/8/11.
 */
public class DetailPresenterImp extends BasePresenter implements IDetailPresenter, DetailModel.onGetDataListener {

    private IDetailView detailView;
    private IDetailModel detailModel;

    public DetailPresenterImp(IDetailView detailView) {
        this.detailView = detailView;
        detailModel = new DetailModel(this);
    }

    @Override
    public void getDetail(String comicUrl) {
        detailView.showProgress();
        addSubscription(detailModel.getDetail(comicUrl));
    }

    @Override
    public void addHistory(Context context,ComicInfo info, String historyName, String historyUrl) {
        detailModel.addHistory(context,info, historyName, historyUrl);
    }

    @Override
    public void updateHistory(Context context,ComicInfo info, String historyName, String historyUrl) {
        detailModel.updateHistory(context,info, historyName, historyUrl);
    }

    @Override
    public void queryHistoryByName(Context context,String name) {
        addSubscription(detailModel.queryHistoryByName(context,name));
    }

    @Override
    public void collectComic(Context context,ComicInfo info) {detailModel.collectComic(context,info);
    }

    @Override
    public void queryCollectByName(Context context,String name) {detailModel.queryCollectByName(context,name);
    }

    @Override
    public void deleteCollectByName(Context context,String name){detailModel.deleteCollectByName(context,name);
    }

    @Override
    public void onGetData(ComicInfo info) {
        detailView.hideProgress();
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
    public void onFail(Throwable throwable) {
        detailView.hideProgress();
        detailView.onFail();
        Logger.e(throwable.getMessage());
    }
}
