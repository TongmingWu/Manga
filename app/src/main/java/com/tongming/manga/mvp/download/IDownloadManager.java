package com.tongming.manga.mvp.download;

import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

/**
 * Author: Tongming
 * Date: 2016/9/13
 */

public interface IDownloadManager {
    void onQueueComplete(String cid);

    void onQueueWait(String cid);

    void onQueueStart(String cid, DownloadInfo info);

    void onQueuePause(String cid);

    void onQueueResume(String cid);

    void onQueueStop(String cid);

    void onQueueFail(String cid);

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
