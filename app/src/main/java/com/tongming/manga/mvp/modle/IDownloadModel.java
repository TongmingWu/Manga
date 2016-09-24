package com.tongming.manga.mvp.modle;

import android.content.Context;

import com.tongming.manga.server.DownloadInfo;

import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public interface IDownloadModel {

    /**
     * ComicDetailActivity和SelectActivity页面使用
     */
    void queryDownloadInfo(Context context, String name, int status);

    void queryDownloadInfo(Context context, String cid);

    /**
     * 下载详情页面使用
     */
    void queryDownloadInfo(Context context, DownloadInfo info);

    /**
     * 下载管理页面使用
     */
    void queryAllDownloadInfo(Context context);

    /**
     * 进入等待队列
     */
    void pushToDownloadQueue(Context context, List<DownloadInfo> info);

    /**
     * 开始单个任务
     */
    void startDownloadTask(Context context, DownloadInfo info);

    /**
     * 开始所有任务
     */
    void startAllDownloadTask(Context context, List<DownloadInfo> info);

    /**
     * 暂停单个任务
     */
    void pauseDownloadTask(Context context, DownloadInfo info);

    /**
     * 暂停所有任务
     */
    void pauseAllDownloadTasks(Context context, DownloadInfo info);

    /**
     * 继续单个任务
     */
    void resumeDownloadTask(Context context, DownloadInfo info);

    /**
     * 取消单个任务
     */
    void cancelDownloadTask(Context context, DownloadInfo info);

    /**
     * 取消所有任务
     */
    void cancelAllDownloadTasks(Context context, DownloadInfo info);
}
