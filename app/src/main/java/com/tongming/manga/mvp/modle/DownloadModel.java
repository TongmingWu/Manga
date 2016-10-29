package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.base.BaseModel;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: Tongming
 * Date: 2016/9/8
 */

public class DownloadModel extends BaseModel implements IDownloadModel {

    private onDownloadListener onDownloadListener;

    public DownloadModel(DownloadModel.onDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
        manager = DBManager.getInstance();
    }

    @Override
    public void queryDownloadInfo(String cid) {
        manager
                .queryDownloadInfoByCid(cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DownloadInfo>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onDownloadListener.onFail(e);
                        this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<DownloadInfo> infoList) {
                        Collections.sort(infoList);
                        onDownloadListener.onQueryDownloadInfo(infoList);
                        this.unsubscribe();
                    }
                });
    }

    @Override
    public void queryDownloadInfo(String name, int status) {
        manager
                .queryDownloadInfo(name, status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DownloadInfo>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onDownloadListener.onFail(e);
                        this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<DownloadInfo> infoList) {
                        onDownloadListener.onQueryDownloadInfo(infoList);
                        this.unsubscribe();
                    }
                });
    }

    @Override
    public void queryDownloadInfo(DownloadInfo info) {

    }

    @Override
    public void queryAllDownloadInfo() {
        manager
                .queryAllDownloadInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DownloadInfo>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onDownloadListener.onFail(e);
                        this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<DownloadInfo> infoList) {
                        onDownloadListener.onQueryAllDownloadInfo(infoList);
                        this.unsubscribe();
                    }
                });
    }

    public interface onDownloadListener {
        void onQueryDownloadInfo(List<DownloadInfo> infoList);

        void onQueryAllDownloadInfo(List<DownloadInfo> infoList);

        void onFail(Throwable throwable);
    }
}
