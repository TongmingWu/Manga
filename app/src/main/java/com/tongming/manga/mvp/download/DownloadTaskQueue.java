package com.tongming.manga.mvp.download;

import com.orhanobut.logger.Logger;
import com.tongming.manga.server.DownloadInfo;

import java.util.ArrayList;
import java.util.Iterator;
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
        int position = taskList.size();
        for (DownloadInfo info : infoList) {
            taskList.add(new DownloadTask(this, info, position));
            position++;
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
            if (task.getStatus() == DownloadInfo.WAIT) {
                task.startTask();
                Logger.d("开始任务");
                break;
            }
        }
    }

    @Override
    public void pauseQueue() {
        if (taskList == null || taskList.size() == 0) {
            return;
        }
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
        for (DownloadTask task : taskList) {
            //继续第一个暂停中的任务,其他暂停任务改为等待
            if (task.getStatus() == DownloadInfo.PAUSE) {
                task.setStatus(DownloadInfo.WAIT);
                position = task.getPosition() < position ? task.getPosition() : position;
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
    public void waitTask(int position) {
        controlTask(position, WAIT_TASK);
        Logger.d("waitTask");
    }

    @Override
    public void startTask(int position) {
        controlTask(position, START_TASK);
        Logger.d("startTask");
    }

    @Override
    public void pauseTask(int position) {
        controlTask(position, PAUSE_TASK);
        Logger.d("pauseTask");
    }

    @Override
    public void resumeTask(int position) {
        controlTask(position, RESUME_TASK);
        Logger.d("resumeTask");
    }

    @Override
    public void stopTask(int position) {
        controlTask(position, STOP_TASK);
        Logger.d("stopTask");
    }

    @Override
    public void restartTask(int position) {
        controlTask(position, RESTART_TASK);
        Logger.d("restartTask");
    }

    private void controlTask(int position, int action) {
        if (taskList == null || taskList.size() <= position) {
            return;
        }
        Iterator<DownloadTask> iterator = taskList.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            if (index == position) {
                DownloadTask task = iterator.next();
                switch (action) {
                    case WAIT_TASK:
                        task.waitTask();
                        break;
                    case START_TASK:
                        task.startTask();
                        break;
                    case PAUSE_TASK:
                        task.pauseTask();
                        break;
                    case RESUME_TASK:
                        task.resumeTask();
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
            index++;
        }
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
        manager.onQueueWait(info.getComic_id());
        setStatus(WAIT);
    }

    @Override
    public void onTaskStart(DownloadInfo info) {
        manager.onQueueStart(cid, info);
        manager.onTaskStart(info);
        setStatus(DOWNLOAD);
    }

    @Override
    public void onTaskPause(DownloadInfo info) {
        manager.onTaskPause(info);
        setStatus(PAUSE);
        //检测队列中是否有等待中的任务,有的话继续下载
        int pauseSize = 0;
        int size = 0;
        for (DownloadTask task : taskList) {
            switch (task.getStatus()) {
                case DownloadInfo.PAUSE:
                    pauseSize++;
                    break;
                case DownloadInfo.WAIT:
                    if (status != DOWNLOAD) {
                        task.startTask();
                        setStatus(DOWNLOAD);
                    }
                default:
                    size++;
                    break;
            }
        }
        if (pauseSize == taskList.size() - size) {
            //所有task暂停
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
        //一个任务下载完成,检测是否有等待中的任务,有的话优先从前面开始
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
                        task.startTask();
                        break;
                    case PAUSE_TASK:
                        task.pauseTask();
                        break;
                    case RESUME_TASK:
                        task.restartTask();
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
