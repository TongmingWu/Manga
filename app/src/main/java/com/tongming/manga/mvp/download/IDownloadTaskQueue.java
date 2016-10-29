package com.tongming.manga.mvp.download;

import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

/**
 * Author: Tongming
 * Date: 2016/9/11
 */

public interface IDownloadTaskQueue {
    void waitQueue();

    void startQueue();

    void pauseQueue();

    void resumeQueue();

    void stopQueue();

    void waitTask(DownloadInfo info);

    void startTask(DownloadInfo info);

    void pauseTask(DownloadInfo info);

    void resumeTask(DownloadInfo info);

    void stopTask(DownloadInfo info);

    void restartTask(DownloadInfo info);

    void onTaskWait(DownloadInfo info);

    void onTaskStart(DownloadInfo info);

    void onTaskPause(DownloadInfo info);

    void onTaskResume(DownloadInfo info);

    void onTaskStop(DownloadInfo info);

    void onTaskRestart(DownloadInfo info);

    void onTaskComplete(DownloadInfo info);

    DBManager getDBManager();

    void onTaskFail(DownloadInfo info);
}
