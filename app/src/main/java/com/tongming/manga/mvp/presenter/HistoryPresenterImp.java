package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.modle.HistoryModel;
import com.tongming.manga.mvp.modle.IHistoryModel;
import com.tongming.manga.mvp.view.activity.IHistoryView;

import java.util.List;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryPresenterImp extends BasePresenter implements IHistoryPresenter, HistoryModel.onQueryListener {
    private IHistoryView historyView;
    private IHistoryModel historyModel;

    public HistoryPresenterImp(IHistoryView historyView) {
        this.historyView = historyView;
        historyModel = new HistoryModel(this);
    }

    @Override
    public void queryAllHistory(Context context) {
        addSubscription(historyModel.queryAllHistory(context));
    }

    @Override
    public void deleteHistoryByName(Context context, String name) {
        historyModel.deleteHistoryByName(context, name);
    }

    @Override
    public void deleteAllHistory(Context context) {
        historyModel.deleteAllHistory(context);
    }

    @Override
    public void onQueryAll(List<HistoryComic> comicList) {
        historyView.onQuery(comicList);
    }

    @Override
    public void onDeleteByName(int state) {
        historyView.onDeleteByName(state);
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
