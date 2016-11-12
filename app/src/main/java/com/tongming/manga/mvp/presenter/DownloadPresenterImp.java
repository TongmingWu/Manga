package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.modle.DownloadModel;
import com.tongming.manga.mvp.view.activity.IDownloadView;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/9/8
 */

public class DownloadPresenterImp extends BasePresenter implements IDownloadPresenter, DownloadModel.onDownloadListener {

    private IDownloadView queryDownloadView;

    public DownloadPresenterImp(IDownloadView queryDownloadView) {
        this.queryDownloadView = queryDownloadView;
        baseModel = new DownloadModel(this);
    }

    @Override
    public void queryDownloadInfo(String name, int status) {
        ((DownloadModel) baseModel).queryDownloadInfo(name, status);
    }

    @Override
    public void queryDownloadInfoByUrl(String chapterUrl) {
        ((DownloadModel) baseModel).queryDownloadInfoByUrl(chapterUrl);
    }

    @Override
    public void queryDownloadInfo(String cid) {
        ((DownloadModel) baseModel).queryDownloadInfo(cid);
    }

    @Override
    public void queryAllDownloadInfo() {
        ((DownloadModel) baseModel).queryAllDownloadInfo();
    }

    @Override
    public void deleteDownloadInfo(String cid) {
        ((DownloadModel) baseModel).deleteDownloadInfo(cid);
    }

    @Override
    public void deleteDownloadInfoByUrl(String url) {
        ((DownloadModel)baseModel).deleteDownloadInfoByUrl(url);
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
    public void onQueryDownloadInfo(ComicPage page) {
        queryDownloadView.onQueryDownloadInfo(page);
    }

    @Override
    public void onDeleteDownloadInfo(int state) {
        queryDownloadView.onDeleteDownloadInfo(state);
    }

    @Override
    public void onFail(Throwable throwable) {
        queryDownloadView.onFail(throwable);
    }
}
