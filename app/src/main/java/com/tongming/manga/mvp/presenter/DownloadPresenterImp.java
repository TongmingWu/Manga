package com.tongming.manga.mvp.presenter;

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
    public void queryDownloadInfo(String name, int status) {
        downloadModel.queryDownloadInfo(name, status);
    }

    @Override
    public void queryDownloadInfo(DownloadInfo info) {
        downloadModel.queryDownloadInfo(info);
    }

    @Override
    public void queryDownloadInfo(String cid) {
        downloadModel.queryDownloadInfo(cid);
    }

    @Override
    public void queryAllDownloadInfo() {
        downloadModel.queryAllDownloadInfo();
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
    public void onFail(Throwable throwable) {
        queryDownloadView.onFail(throwable);
    }
}
