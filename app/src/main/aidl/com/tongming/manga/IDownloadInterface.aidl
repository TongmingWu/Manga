// IDownloadInterface.aidl
package com.tongming.manga;
import com.tongming.manga.DownloadInfo;

// Declare any non-default types here with import statements

interface IDownloadInterface {
    //单个任务控制
    void waitTask(DownloadInfo info,int position);
    void startTask(DownloadInfo info,int position);
    void pauseTask(DownloadInfo info,int position);
    void resumeTask(DownloadInfo info,int position);
    void stopTask(DownloadInfo info,int position);
    void restartTask(DownloadInfo info,int position);

    //队列控制
    void waitQueue(int cid);
    void startQueue(int cid);
    void pauseQueue(int cid);
    void stopQueue(int cid);
}
