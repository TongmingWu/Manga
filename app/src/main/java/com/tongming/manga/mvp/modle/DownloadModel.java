package com.tongming.manga.mvp.modle;

import android.content.Context;

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
    public void queryDownloadInfo(Context context, String cid) {
        final DBManager manager = new DBManager(context);
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
    public void queryDownloadInfo(Context context, String name, int status) {
        new DBManager(context)
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
    public void queryDownloadInfo(Context context, DownloadInfo info) {

    }

    @Override
    public void queryAllDownloadInfo(Context context) {
        new DBManager(context)
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

    @Override
    public void pushToDownloadQueue(Context context, List<DownloadInfo> info) {

    }

    @Override
    public void startDownloadTask(Context context, DownloadInfo info) {
        //调用ApiManager下载图片,调用DBManager将下载信息存入sqlite

    }

    @Override
    public void startAllDownloadTask(Context context, List<DownloadInfo> info) {

    }

    @Override
    public void pauseDownloadTask(Context context, DownloadInfo info) {

    }

    @Override
    public void pauseAllDownloadTasks(Context context, DownloadInfo info) {

    }

    @Override
    public void resumeDownloadTask(Context context, DownloadInfo info) {

    }

    @Override
    public void cancelDownloadTask(Context context, DownloadInfo info) {

    }

    @Override
    public void cancelAllDownloadTasks(Context context, DownloadInfo info) {

    }

    public interface onDownloadListener {
        void onQueryDownloadInfo(List<DownloadInfo> infoList);

        void onQueryAllDownloadInfo(List<DownloadInfo> infoList);

        void onPushToDownloadQueue();

        void onStartDownloadTask();

        void onPauseDownloadTask();

        void onPauseAllDownloadTasks();

        void onCancelDownloadTask();

        void onCancelAllDownloadTasks();

        void onFail(Throwable throwable);
    }
}
