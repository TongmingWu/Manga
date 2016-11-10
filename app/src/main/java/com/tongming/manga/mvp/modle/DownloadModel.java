package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.base.BaseModel;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
    public void queryDownloadInfoByUrl(String chapterUrl) {
        manager.queryDownloadInfo(chapterUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<List<DownloadInfo>, ComicPage>() {
                    @Override
                    public ComicPage call(List<DownloadInfo> infoList) {
                        if (infoList.size() > 0) {
                            ComicPage page = new ComicPage();
                            DownloadInfo info = infoList.get(0);
                            page.setChapter_name(info.getChapter_name());
                            page.setComic_source(info.getComic_source());
                            page.setCurrent_chapter_url(info.getChapter_url());
                            page.setNext(info.getNext() == 1);
                            page.setPrepare(info.getPrepare() == 1);
                            page.setNext_chapter_url(info.getNext_url());
                            page.setPre_chapter_url(info.getPre_url());
                            page.setComic_name(info.getComic_name());
                            return page;
                        }
                        return null;
                    }
                })
                .subscribe(new Subscriber<ComicPage>() {
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
                    public void onNext(ComicPage page) {
                        onDownloadListener.onQueryDownloadInfo(page);
                        this.unsubscribe();
                    }
                });
    }

    @Override
    public void queryAllDownloadInfo() {
        manager.queryAllDownloadInfo()
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

        void onQueryDownloadInfo(ComicPage page);

        void onQueryAllDownloadInfo(List<DownloadInfo> infoList);

        void onFail(Throwable throwable);
    }
}
