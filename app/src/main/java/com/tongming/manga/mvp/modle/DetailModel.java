package com.tongming.manga.mvp.modle;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.db.DBManager;

import java.util.List;

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
    public Subscription addHistory(final ComicInfo info, final String historyName, final String historyUrl) {
        return DBManager.getInstance().addHistory(info, historyName, historyUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        onGetDataListener.onAddHistoryCompleted(aLong);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onGetDataListener.onFail(throwable);
                    }
                });
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
    public Subscription updateHistory(ComicInfo info, String historyName, String historyUrl) {
        return DBManager.getInstance().updateHistory(info, historyName, historyUrl)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        onGetDataListener.onUpdateHistoryCompleted(integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onGetDataListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription queryHistoryByName(String comicName) {
        Logger.d("开始读取历史记录");
        return DBManager.getInstance().queryHistoryByName(comicName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HistoryComic>>() {
                    @Override
                    public void call(List<HistoryComic> historyComics) {
                        if (historyComics.size() > 0) {
                            Logger.d("得到的历史记录:" + historyComics.get(0).getHistoryUrl());
                            for (HistoryComic comic : historyComics) {
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
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onGetDataListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription collectComic(final ComicInfo info) {
        //先查询数据库中是否已有记录,有的话更新,没有则插入
        return DBManager.getInstance().queryCollectedByName(info.getComic_name())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CollectedComic>>() {
                    @Override
                    public void call(List<CollectedComic> collectedComics) {
                        if (collectedComics.size() == 0) {
                            DBManager.getInstance().collectComic(info)
                                    .observeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Long>() {
                                        @Override
                                        public void call(Long aLong) {
                                            onGetDataListener.onAddCollectCompleted(aLong);
                                        }
                                    }, new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            onGetDataListener.onFail(throwable);
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public Subscription queryCollectByName(String name) {
        return DBManager.getInstance().queryCollectedByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<CollectedComic>>() {
                    @Override
                    public void call(List<CollectedComic> collectedComics) {
                        if (collectedComics.size() > 0) {
                            onGetDataListener.onQueryCollectCompleted(true);
                        } else {
                            onGetDataListener.onQueryCollectCompleted(false);
                        }
                    }
                });
    }

    @Override
    public Subscription deleteCollectByName(String name) {
        return DBManager.getInstance().deleteCollectByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        onGetDataListener.onDeleteCollectCompleted(integer);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onGetDataListener.onFail(throwable);
                    }
                });
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
