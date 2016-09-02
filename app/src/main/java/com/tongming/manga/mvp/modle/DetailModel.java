package com.tongming.manga.mvp.modle;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/11.
 */
public class DetailModel implements IDetailModel {
    private onGetDataListener onGetDataListener;

    public DetailModel(DetailModel.onGetDataListener onGetDataListener) {
        this.onGetDataListener = onGetDataListener;
    }

    @Override
    public Subscription getDetail(String comicUrl) {
        return ApiManager.getInstance().getComicInfo(comicUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ComicInfo>() {
                    @Override
                    public void call(ComicInfo info) {
                        onGetDataListener.onGetData(info);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onGetDataListener.onFail(throwable);
                    }
                });
    }

    @Override
    public void addHistory(Context context, final ComicInfo info, final String historyName, final String historyUrl) {
        long state = new DBManager(context).addHistory(info, historyName, historyUrl);
        onGetDataListener.onAddHistoryCompleted(state);
        //git
        /*return DBManager.getInstance().queryHistoryByName(info.getComic_name())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HistoryComic>>() {
                    @Override
                    public void call(List<HistoryComic> comics) {
                        if (comics.size() > 0) {
                            //如果数据库中有记录的话更新记录
                            updateHistory(info, historyName, historyUrl);
                        } else {
                        }
                    }
                });*/
    }

    @Override
    public void updateHistory(Context context, ComicInfo info, String historyName, String historyUrl) {
        int state = new DBManager(context).updateHistory(info, historyName, historyUrl);
        onGetDataListener.onUpdateHistoryCompleted(state);
    }

    @Override
    public Subscription queryHistoryByName(Context context, final String comicName) {
        Logger.d("开始读取历史记录");
        return new DBManager(context).queryHistoryByName(comicName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HistoryComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetDataListener.onFail(e);
                    }

                    @Override
                    public void onNext(List<HistoryComic> comics) {
                        if (comics.size() > 0) {
                            Logger.d("得到的历史记录:" + comics.get(0).getHistoryUrl());
                            for (HistoryComic comic : comics) {
                                if (!TextUtils.isEmpty(comic.getHistoryUrl())) {
                                    onGetDataListener.onQueryHistoryCompleted(
                                            comic.getHistoryName(),
                                            comic.getHistoryUrl());
                                }
                            }
                        } else {
                            onGetDataListener.onQueryHistoryCompleted("", "");
                        }
                    }
                });
    }

    @Override
    public void collectComic(Context context, final ComicInfo info) {
        //TODO 向服务器更新数据

        long state = new DBManager(context).collectComic(info);
        onGetDataListener.onAddCollectCompleted(state);
    }

    @Override
    public Subscription queryCollectByName(Context context, String name) {
        return new DBManager(context).queryCollectedByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetDataListener.onFail(e);
                    }

                    @Override
                    public void onNext(List<CollectedComic> comics) {
                        if (comics.size() > 0) {
                            onGetDataListener.onQueryCollectCompleted(true);
                        }
                    }
                });
    }

    @Override
    public void deleteCollectByName(Context context, String name) {
        int state = new DBManager(context).deleteCollectByName(name);
        onGetDataListener.onDeleteCollectCompleted(state);
    }

    public interface onGetDataListener {
        void onGetData(ComicInfo info);

        void onAddHistoryCompleted(long state);

        void onUpdateHistoryCompleted(int state);

        void onQueryHistoryCompleted(String historyName, String historyUrl);

        void onAddCollectCompleted(long state);

        void onQueryCollectCompleted(boolean isCollected);

        void onDeleteCollectCompleted(int state);

        void onFail(Throwable throwable);
    }
}
