package com.tongming.manga.mvp.presenter;

import android.content.Context;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.modle.DownloadModel;
import com.tongming.manga.mvp.modle.IDownloadModel;
import com.tongming.manga.mvp.view.activity.IQueryDownloadView;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/9/8
 */

public class DownloadPresenterImp extends BasePresenter implements IDownloadPresenter, DownloadModel.onDownloadListener {

    private IQueryDownloadView queryDownloadView;
    private IDownloadModel downloadModel;

    public DownloadPresenterImp(IQueryDownloadView queryDownloadView) {
        this.queryDownloadView = queryDownloadView;
        downloadModel = new DownloadModel(this);
    }

    @Override
    public void queryDownloadInfo(Context context, String name, int status) {
        downloadModel.queryDownloadInfo(context, name, status);
    }

    @Override
    public void queryDownloadInfo(Context context, DownloadInfo info) {
        downloadModel.queryDownloadInfo(context, info);
    }

    @Override
    public void queryDownloadInfo(Context context, String cid) {
        downloadModel.queryDownloadInfo(context, cid);
    }

    @Override
    public void queryAllDownloadInfo(Context context) {
        downloadModel.queryAllDownloadInfo(context);
    }

    @Override
    public void pushToDownloadQueue(Context context, List<DownloadInfo> info) {
        downloadModel.pushToDownloadQueue(context, info);
    }

    @Override
    public void startDownloadTask(Context context, DownloadInfo info) {
        downloadModel.startDownloadTask(context, info);
    }

    @Override
    public void startAllDownloadTask(Context context, List<DownloadInfo> info) {
        downloadModel.startAllDownloadTask(context, info);
    }

    @Override
    public void pauseDownloadTask(Context context, DownloadInfo info) {
        downloadModel.pauseDownloadTask(context, info);
    }

    @Override
    public void pauseAllDownloadTask(Context context, DownloadInfo info) {
        downloadModel.pauseAllDownloadTasks(context, info);
    }

    @Override
    public void resumeDownloadTask(Context context, DownloadInfo info) {
        downloadModel.resumeDownloadTask(context, info);
    }

    @Override
    public void cancelDownloadTask(Context context, DownloadInfo info) {
        downloadModel.cancelDownloadTask(context, info);
    }

    @Override
    public void cancelAllDownloadTasks(Context context, DownloadInfo info) {
        downloadModel.cancelAllDownloadTasks(context, info);
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> infoList) {
        queryDownloadView.onQueryDownloadInfo(infoList);
    }

    @Override
    public void onQueryAllDownloadInfo(List<DownloadInfo> infoList) {
        queryDownloadView.onQueryDownloadInfo(infoList);
    }

    @Override
    public void onPushToDownloadQueue() {

    }

    @Override
    public void onStartDownloadTask() {

    }

    @Override
    public void onPauseDownloadTask() {

    }

    @Override
    public void onPauseAllDownloadTasks() {

    }

    @Override
    public void onCancelDownloadTask() {

    }

    @Override
    public void onCancelAllDownloadTasks() {

    }

    @Override
    public void onFail(Throwable throwable) {
        queryDownloadView.onFail(throwable);
    }
}
