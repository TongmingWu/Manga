package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.modle.HistoryModel;
import com.tongming.manga.mvp.view.activity.IHistoryView;

import java.util.List;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryPresenterImp extends BasePresenter implements IHistoryPresenter, HistoryModel.onQueryListener {
    private IHistoryView historyView;

    public HistoryPresenterImp(IHistoryView historyView) {
        this.historyView = historyView;
        baseModel = new HistoryModel(this);
    }

    @Override
    public void queryAllHistory() {
        addSubscription(((HistoryModel) baseModel).queryAllHistory());
    }

    @Override
    public void deleteHistoryByName(String name) {
        ((HistoryModel) baseModel).deleteHistoryByName(name);
    }

    @Override
    public void restoreHistory(HistoryComic comic) {
        ((HistoryModel) baseModel).restoreHistory(comic);
    }

    @Override
    public void deleteAllHistory() {
        ((HistoryModel) baseModel).deleteAllHistory();
    }

    @Override
    public void onQueryAll(List<HistoryComic> comicList) {
        historyView.onQuery(comicList);
    }

    @Override
    public void onDeleteByName(int state, String name) {
        historyView.onDeleteByName(state, name);
    }

    @Override
    public void onRestoreHistory(int state) {
        historyView.onRestoreHistory(state);
    }

    @Override
    public void onDeleteAll(int state) {
        historyView.onDeleteAll(state);
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.d(throwable.getMessage());
    }
}
