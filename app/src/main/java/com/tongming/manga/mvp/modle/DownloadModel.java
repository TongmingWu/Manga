package com.tongming.manga.mvp.modle;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.base.BaseModel;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.io.File;
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
        manager.queryDownloadInfoByCid(cid)
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
        manager.queryDownloadInfo(name, status)
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
                            return parserPage(infoList.get(0));
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

    private ComicPage parserPage(DownloadInfo info) {
        ComicPage page = new ComicPage();
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

    @Override
    public void deleteDownloadInfo(final String cid) {
        manager.queryDownloadInfoByCid(cid)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<List<DownloadInfo>, Boolean>() {
                    @Override
                    public Boolean call(List<DownloadInfo> infoList) {
                        String path = BaseApplication.getExternalPath() + "/download/";
                        boolean result = false;
                        for (DownloadInfo info : infoList) {
                            File file = new File(path + info.getComic_name());
                            result = deleteFile(file);
                        }
                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        onDownloadListener.onFail(e);
                    }

                    @Override
                    public void onNext(Boolean bool) {
                        int state = manager.deleteDownloadInfo(cid);
                        onDownloadListener.onDeleteDownloadInfo(state);
                        this.unsubscribe();
                    }
                });
    }

    private Boolean deleteFile(File file) {
        boolean result = false;
        if (file.isFile()) {
            result = file.delete();
            return result;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                result = file.delete();
                return result;
            }

            for (File child : childFiles) {
                result = deleteFile(child);
            }
        }
        result = file.delete();
        return result;
    }

    @Override
    public void deleteDownloadInfoByUrl(final String url) {
        manager.queryDownloadInfo(url)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<List<DownloadInfo>, Boolean>() {
                    @Override
                    public Boolean call(List<DownloadInfo> infoList) {
                        String path = BaseApplication.getExternalPath() + "/download/";
                        boolean result = false;
                        for (DownloadInfo info : infoList) {
                            File file = new File(path + info.getComic_name() + "/" + info.getChapter_name());
                            result = deleteFile(file);
                        }
                        return result;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        onDownloadListener.onFail(e);
                    }

                    @Override
                    public void onNext(Boolean bool) {
                        int state = manager.deleteDownloadInfoByUrl(url);
                        onDownloadListener.onDeleteDownloadInfo(state);
                        this.unsubscribe();
                    }
                });
    }

    public interface onDownloadListener {
        void onQueryDownloadInfo(List<DownloadInfo> infoList);

        void onQueryDownloadInfo(ComicPage page);

        void onQueryAllDownloadInfo(List<DownloadInfo> infoList);

        void onDeleteDownloadInfo(int state);

        void onFail(Throwable throwable);
    }
}
