// IDownloadInterface.aidl
package com.tongming.manga.server;
import com.tongming.manga.server.DownloadInfo;

// Declare any non-default types here with import statements

interface IDownloadInterface {
    //单个任务控制
    void waitTask(in DownloadInfo info);
//    void waitTaskByPos(int position);
    void startTask(in DownloadInfo info);
//    void startTaskByPos(int position);
    void pauseTask(in DownloadInfo info);
//    void pauseTaskByPos(int position);
    void resumeTask(in DownloadInfo info);
//    void resumeTaskByPos(int position);
    void stopTask(in DownloadInfo info);
//    void stopTaskByPos(int position);
    void restartTask(in DownloadInfo info);
//    void restartTaskByPos(int position);

    //队列控制
    void waitQueue(String cid);
    void startQueue(String cid);
    void pauseQueue(String cid);
    void resumeQueue(String cid);
    void stopQueue(String cid);
}
