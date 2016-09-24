package com.tongming.manga.mvp.download;

/**
 * Author: Tongming
 * Date: 2016/9/9
 */

public interface IDownloadTask {

    /**
     * 任务进入等待状态
     */
    void waitTask();

    /**
     * 开始任务
     */
    void startTask();

    /**
     * 暂停任务
     */
    void pauseTask();

    /**
     * 继续任务
     */
    void resumeTask();

    /**
     * 停止任务
     */
    void stopTask();

    /**
     * 重新下载
     */
    void restartTask();
}
