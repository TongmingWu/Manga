package com.tongming.manga.mvp.download;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.server.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Tongming
 * Date: 2016/9/9
 */

public class DownloadTaskQueue implements IDownloadTaskQueue, Comparable {

    public static final int WAIT_TASK = 0;
    public static final int START_TASK = 1;
    public static final int PAUSE_TASK = 2;
    public static final int RESUME_TASK = 3;
    public static final int STOP_TASK = 4;
    public static final int RESTART_TASK = 5;

    public static final int WAIT = 0x01;
    public static final int DOWNLOAD = 0x02;
    public static final int PAUSE = 0x03;
    public static final int COMPLETE = 0x04;
    public static final int STOP = 0x05;

    private IDownloadManager manager;
    private String cid;
    private List<DownloadInfo> infoList;
    private List<DownloadTask> taskList;
    private int status = 0x01;
    private boolean isPauseAll;
    private boolean isPauseOne;

    /**
     * 构造一个Queue
     *
     * @param cid 漫画id
     */
    public DownloadTaskQueue(IDownloadManager manager, String cid) {
        this.manager = manager;
        this.cid = cid;
    }

    /**
     * 构造一个Queue
     *
     * @param cid      漫画id
     * @param infoList 下载列表
     */
    public DownloadTaskQueue(IDownloadManager manager, String cid, List<DownloadInfo> infoList) {
        this.manager = manager;
        this.cid = cid;
        addTask(infoList);
    }

    public void addTask(List<DownloadInfo> infoList) {
        if (infoList == null || infoList.size() == 0) {
            return;
        }
        if (this.infoList == null) {
            this.infoList = new ArrayList<>();
        }
        if (taskList == null) {
            taskList = new ArrayList<>();
        }
        this.infoList.addAll(infoList);
        for (DownloadInfo info : infoList) {
            taskList.add(new DownloadTask(this, info));
        }
        Logger.d("addTask");
    }

    @Override
    public void waitQueue() {
        if (taskList == null || taskList.size() == 0) {
            return;
        }
        setStatus(WAIT);
        for (DownloadTask task : taskList) {
            task.waitTask();
        }
        Logger.d("waitQueue");
    }

    @Override
    public void startQueue() {
        if (taskList == null || taskList.size() == 0) {
            return;
        }
        if (status == DOWNLOAD) {
            return;
        }
        for (DownloadTask task : taskList) {
            //判断有问题
            if (task.getStatus() == DownloadInfo.WAIT) {
                if (status != DOWNLOAD) {
                    task.startTask();
                    status = DOWNLOAD;
                    Logger.d("开始任务");
                } else {
                    task.waitTask();
                }
            }
        }
    }

    @Override
    public void pauseQueue() {
        if (taskList == null || taskList.size() == 0) {
            return;
        }
        isPauseAll = true;
        for (DownloadTask task : taskList) {
            task.pauseTask();
        }
        Logger.d("pauseQueue");
    }

    @Override
    public void resumeQueue() {
        if (taskList == null || taskList.size() == 0) {
            return;
        }
        int position = Integer.MAX_VALUE;
        for (int index = 0; index < taskList.size(); index++) {
            //继续第一个暂停中的任务,其他暂停任务改为等待
            DownloadTask task = taskList.get(index);
            if (task.getStatus() == DownloadInfo.PAUSE) {
                task.waitTask();
                position = index < position ? index : position;
            }
        }
        if (position < taskList.size()) {
            taskList.get(position).resumeTask();
        }
        Logger.d("resumeQueue");
    }

    @Override
    public void stopQueue() {
        if (taskList == null || taskList.size() == 0) {
            return;
        }
        for (DownloadTask task : taskList) {
            task.stopTask();
        }
        taskList.clear();
        infoList.clear();
        Logger.d("stopQueue");
    }

    @Override
    public void waitTask(DownloadInfo info) {
        controlTask(info, WAIT_TASK);
    }

    @Override
    public void startTask(DownloadInfo info) {
        controlTask(info, START_TASK);
    }

    @Override
    public void pauseTask(DownloadInfo info) {
        controlTask(info, PAUSE_TASK);
    }

    @Override
    public void resumeTask(DownloadInfo info) {
        controlTask(info, RESUME_TASK);
    }

    @Override
    public void stopTask(DownloadInfo info) {
        controlTask(info, STOP_TASK);
    }

    @Override
    public void restartTask(DownloadInfo info) {
        controlTask(info, RESTART_TASK);
    }

    @Override
    public void onTaskWait(DownloadInfo info) {
        manager.onTaskWait(info);
        int waitSize = 0;
        int pauseSize = 0;
        boolean isDownload = false;
        for (DownloadTask task : taskList) {
            switch (task.getStatus()) {
                case DownloadInfo.DOWNLOAD:
                    isDownload = true;
                    break;
                case DownloadInfo.WAIT:
                    waitSize++;
                    break;
                case DownloadInfo.PAUSE:
                    pauseSize++;
                    break;
            }
        }
        if (!isDownload && waitSize == taskList.size() - pauseSize) {
            Logger.d("全部等待");
            manager.onQueueWait(info.getComic_id());
            setStatus(WAIT);
        }
    }

    @Override
    public void onTaskStart(DownloadInfo info) {
        manager.onQueueStart(cid, info);
        manager.onTaskStart(info);
        setStatus(DOWNLOAD);
    }

    @Override
    public synchronized void onTaskPause(DownloadInfo info) {
        manager.onTaskPause(info);
        //检测队列中是否有等待中的任务,有的话继续下载
        int pauseSize = 0;
        for (DownloadTask task : taskList) {
            switch (task.getStatus()) {
                case DownloadInfo.PAUSE:
                    pauseSize++;
                    break;
                case DownloadInfo.WAIT:
                    if (!isPauseAll && isPauseOne) {
                        task.startTask();   //单个暂停出现所有等待中的任务开始下载
                        status = DOWNLOAD;
                        isPauseOne = false;
                    }
                    break;
            }
        }
        if (status != PAUSE && pauseSize == taskList.size()) {
            //所有task暂停
            Logger.d("所有任务暂停");
            setStatus(PAUSE);
            isPauseAll = false;
            manager.onQueuePause(cid);
        }
    }

    @Override
    public void onTaskResume(DownloadInfo info) {
        manager.onTaskResume(info);
        setStatus(DOWNLOAD);
    }

    @Override
    public void onTaskStop(DownloadInfo info) {
        manager.onTaskStop(info);
        setStatus(STOP);
    }

    @Override
    public void onTaskRestart(DownloadInfo info) {
        manager.onTaskRestart(info);
        setStatus(DOWNLOAD);
    }

    @Override
    public void onTaskComplete(DownloadInfo info) {
        //一个任务下载完成,将其移出队列,检测是否有等待中的任务,有的话优先从前面开始
        for (DownloadTask task : taskList) {
            if (info.hashCode() == task.hashCode()) {
                taskList.remove(task);
                break;
            }
        }
        manager.onTaskComplete(info);
        nextTask();
    }

    @Override
    public void onTaskFail(DownloadInfo info) {
        //一个任务下载失败,检测是否有等待中的任务,有的话优先从前面开始
        manager.onTaskFail(info);
        nextTask();
    }

    private void nextTask() {
        for (DownloadTask task : taskList) {
            if (task.getInfo().getStatus() == DownloadInfo.WAIT) {
                task.startTask();
                Logger.d("开始下一个任务");
                return;
            }
        }
        //队列中已没有等待中的任务,回调Manager中的onQueueComplete()
        setStatus(COMPLETE);
        manager.onQueueComplete(cid);
    }

    private void controlTask(DownloadInfo info, int action) {
        if (taskList == null) {
            return;
        }
        for (DownloadTask task : taskList) {
            if (task.hashCode() == info.hashCode()) {
                switch (action) {
                    case WAIT_TASK:
                        task.waitTask();
                        break;
                    case START_TASK:
                        if (status != DOWNLOAD) {
                            task.startTask();
                            setStatus(DOWNLOAD);
                        } else {
                            task.waitTask();
                        }
                        break;
                    case PAUSE_TASK:
                        isPauseOne = true;
                        task.pauseTask();
                        break;
                    case RESUME_TASK:
                        if (status != DOWNLOAD) {
                            task.resumeTask();
                            setStatus(DOWNLOAD);
                        } else {
                            task.waitTask();
                        }
                        break;
                    case STOP_TASK:
                        task.stopTask();
                        break;
                    case RESTART_TASK:
                        task.restartTask();
                        break;
                    default:
                        break;
                }
                break;
            }
        }
    }

    @Override
    public DBManager getDBManager() {
        return manager.getDBManager();
    }

    public List<DownloadTask> getTaskList() {
        return taskList;
    }

    public String getCid() {
        return cid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {

        return status;
    }

    @Override
    public int hashCode() {
        return (cid + "").hashCode();
    }

    @Override
    public int compareTo(Object o) {
        DownloadTaskQueue queue = (DownloadTaskQueue) o;
        return this.cid.hashCode() - queue.cid.hashCode();
    }
}
