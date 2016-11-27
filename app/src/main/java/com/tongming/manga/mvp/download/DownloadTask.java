package com.tongming.manga.mvp.download;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

public class DownloadTask implements IDownloadTask {

    private DownloadInfo info;
    private IDownloadTaskQueue queue;
    private int status;
    private DBManager manager;
    private List<String> imgs;
    private String downloadPath;


    public DownloadTask(IDownloadTaskQueue queue, DownloadInfo info) {
        this.queue = queue;
        this.info = info;
        downloadPath = BaseApplication.getExternalPath()
                + "/download/" + info.getComic_name()
                + "/" + info.getChapter_name();
        manager = queue.getDBManager();
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
                        this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<DownloadInfo> infoList) {
                        if (infoList.size() == 0) {
                            int state = manager.insertDownloadInfo(info);
                            //创建目录
                            File file = new File(downloadPath);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                        } else {
                            updateDownloadInfo();
                            info.setPosition(info.getPosition());
                        }
                        this.unsubscribe();
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
                .getComicPage(info.getComic_source(), info.getChapter_url())
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
                        info.setStatus(DownloadInfo.FAIL);
                        queue.onTaskFail(info);
                    }

                    @Override
                    public void onNext(List<String> list) {
                        if (status != DownloadInfo.WAIT || status != DownloadInfo.FAIL || status != DownloadInfo.PAUSE) {
                            info.setTotal(list.size());
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
                .downloadImage(imgs.get(info.getPosition()), info.getComic_source())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, File>() {
                    @Override
                    public File call(ResponseBody responseBody) {
                        return saveImage(responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        //回调Queue的onTaskFail()
                        setStatus(DownloadInfo.FAIL);
                        Logger.e(e.getMessage());   //409
                        queue.onTaskFail(info);
                    }

                    @Override
                    public void onNext(File file) {
                        updateSchedule();
                    }
                });
    }

    private File saveImage(ResponseBody body) {
        String imgPath = downloadPath + "/" + info.getPosition() + ".jpg";
        File file = new File(imgPath);
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = body.byteStream();
            fos = new FileOutputStream(file);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
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
            }
        }
    }

    private void updateDownloadInfo() {
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

}
