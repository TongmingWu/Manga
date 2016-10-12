package com.tongming.manga.mvp.download;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Author: Tongming
 * Date: 2016/9/9
 */

public class DownloadTask implements IDownloadTask, Comparable {

    private DownloadInfo info;
    private DownloadTaskQueue queue;
    private int position;
    private int status;
    private DBManager manager;
    private List<String> imgs;


    public DownloadTask(DownloadTaskQueue queue, DownloadInfo info, int position) {
        this.queue = queue;
        this.info = info;
        this.position = position;
        manager = new DBManager(BaseApplication.getContext());
        insertDownloadInfo();
    }

    private void insertDownloadInfo() {
        manager.queryDownloadInfo(info.getChapter_url())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<List<DownloadInfo>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<DownloadInfo> infoList) {
                        if (infoList.size() == 0) {
                            Logger.d("插入记录");
                            int state = manager.insertDownloadInfo(info);
                            this.unsubscribe();
                        } else {
                            Logger.d("存在");
                            updateDownloadInfo();
                            info.setPosition(infoList.get(0).getPosition());
                            this.unsubscribe();
                        }
                    }
                });
    }

    @Override
    public void waitTask() {
        setStatus(DownloadInfo.WAIT);
        updateDownloadInfo();
        queue.onTaskWait(info);
    }

    @Override
    public void startTask() {
        setStatus(DownloadInfo.DOWNLOAD);
        updateDownloadInfo();
        ApiManager.getInstance()
                .getComicPage(info.getChapter_url())
                .map(new Func1<ComicPage, List<String>>() {
                    @Override
                    public List<String> call(ComicPage comicPage) {
                        return comicPage.getImg_list();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e(e.getMessage());
                        queue.onTaskFail(info);
                    }

                    @Override
                    public void onNext(List<String> list) {
                        if (status != DownloadInfo.WAIT || status != DownloadInfo.FAIL || status != DownloadInfo.PAUSE) {
                            info.setTotal(list.size());
//                            setStatus(DownloadInfo.DOWNLOAD);
                            updateDownloadInfo();
                            imgs = list;
                            downloadImage();
                            queue.onTaskStart(info);
                        } else {
                            this.unsubscribe();
                        }
                    }
                });
    }

    private void downloadImage() {
        ApiManager.getInstance()
                .downloadImage(imgs.get(info.getPosition()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //回调Queue的onTaskFail()
                        setStatus(DownloadInfo.FAIL);
                        queue.onTaskFail(info);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        updateSchedule();
                    }
                });
    }

    private void updateSchedule() {
        if (status == DownloadInfo.DOWNLOAD) {
            info.setPosition(info.getPosition() + 1);
            if (info.getPosition() < info.getTotal()) {
                updateDownloadInfo();
                queue.onTaskStart(info);
                downloadImage();
            } else {
                setStatus(DownloadInfo.COMPLETE);
                updateDownloadInfo();
                //回调Queue的onTaskComplete()
                queue.onTaskComplete(info);
                manager.closeDB();
            }
        }
    }

    private void updateDownloadInfo() {
        //TODO 出现内存抖动频繁的问题
        if (manager != null) {
            manager.updateDownloadInfo(info);
        }
    }

    @Override
    public void pauseTask() {
        if (status != DownloadInfo.COMPLETE) {
            setStatus(DownloadInfo.PAUSE);
            updateDownloadInfo();
            queue.onTaskPause(info);
        }
    }

    @Override
    public void resumeTask() {
        if (status != DownloadInfo.COMPLETE && status != DownloadInfo.DOWNLOAD) {
            setStatus(DownloadInfo.DOWNLOAD);
            updateDownloadInfo();
            //imgs对象被回收,需要重新创建
            startTask();
            queue.onTaskResume(info);
        }
    }

    @Override
    public void stopTask() {
        //删除任务
        setStatus(DownloadInfo.STOP);
        manager.deleteDownloadInfo(info);
        queue.onTaskStop(info);
    }

    @Override
    public void restartTask() {
        setStatus(DownloadInfo.DOWNLOAD);
        updateDownloadInfo();
        queue.onTaskRestart(info);
    }

    public int getPosition() {
        return position;
    }

    public DownloadInfo getInfo() {
        return info;
    }

    public void setStatus(int status) {
        info.setStatus(status);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return info.getChapter_url().hashCode();
    }


    @Override
    public int compareTo(Object o) {
        DownloadTask task = (DownloadTask) o;
        return this.position - task.position;
    }
}
