package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author: Tongming
 * Date: 2016/9/8
 */

public class DownloadModel implements IDownloadModel {

    private onDownloadListener onDownloadListener;

    public DownloadModel(DownloadModel.onDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
    }

    @Override
    public void queryDownloadInfo(String cid) {
        final DBManager manager = DBManager.getInstance();
        manager
                .queryDownloadInfoByCid(cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DownloadInfo>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                        manager.closeDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onDownloadListener.onFail(e);
                        manager.closeDB();
                    }

                    @Override
                    public void onNext(List<DownloadInfo> infoList) {
                        Collections.sort(infoList);
                        onDownloadListener.onQueryDownloadInfo(infoList);
                    }
                });
    }

    @Override
    public void queryDownloadInfo(String name, int status) {
        DBManager.getInstance()
                .queryDownloadInfo(name, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DownloadInfo>>() {
                    @Override
                    public void call(List<DownloadInfo> infoList) {
                        onDownloadListener.onQueryDownloadInfo(infoList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onDownloadListener.onFail(throwable);
                    }
                });
    }

    @Override
    public void queryDownloadInfo(DownloadInfo info) {

    }

    @Override
    public void queryAllDownloadInfo() {
        DBManager.getInstance()
                .queryAllDownloadInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DownloadInfo>>() {
                    @Override
                    public void call(List<DownloadInfo> infoList) {
                        onDownloadListener.onQueryAllDownloadInfo(infoList);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onDownloadListener.onFail(throwable);
                    }
                });
    }

    public interface onDownloadListener {
        void onQueryDownloadInfo(List<DownloadInfo> infoList);

        void onQueryAllDownloadInfo(List<DownloadInfo> infoList);

        void onFail(Throwable throwable);
    }
}
