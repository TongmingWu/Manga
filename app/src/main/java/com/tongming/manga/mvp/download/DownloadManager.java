package com.tongming.manga.mvp.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.server.IDownloadInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Author: Tongming
 * Date: 2016/9/9
 */

public class DownloadManager extends Service implements IDownloadManager {

    public static final int WAIT_QUEUE = 0;
    public static final int START_QUEUE = 1;
    public static final int PAUSE_QUEUE = 2;
    public static final int RESUME_QUEUE = 3;
    public static final int STOP_QUEUE = 4;

    private Set<DownloadTaskQueue> queueList;
    private DownloadBinder binder;
    private OnQueueListener onQueueListener;
    private onTaskListener onTaskListener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new DownloadBinder();
        return binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ComicInfo info = intent.getParcelableExtra("info");
        List<Integer> pos = intent.getIntegerArrayListExtra("pos");
        //加入下载队列
        if (info != null && pos != null) {
            String cid = info.getComic_id();
            DownloadTaskQueue queue = checkQueue(cid);
            if (queue != null) {
                //已存在下载队列
                queue.addTask(convertToDownload(info, pos));
            } else {
                queue = createQueue(cid, convertToDownload(info, pos));
            }
            //检测是否有下载中的队列,启动下载任务
            if (!hasDownloadQueue()) {
                queue.startQueue();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private DownloadTaskQueue createQueue(String cid, List<DownloadInfo> infoList) {
        DownloadTaskQueue queue = new DownloadTaskQueue(this, cid, infoList);
        queueList.add(queue);
        return queue;
    }

    /**
     * 检查队列是否存在
     *
     * @param cid 漫画id
     */
    private DownloadTaskQueue checkQueue(String cid) {
        if (queueList == null) {
            queueList = new TreeSet<>();
            return null;
        }
        if (queueList.size() == 0) {
            return null;
        }
        for (DownloadTaskQueue queue : queueList) {
            if (cid.equals(queue.getCid())) {
                return queue;
            }
        }
        return null;
    }

    private boolean hasDownloadQueue() {
        if (queueList != null) {
            for (DownloadTaskQueue queue : queueList) {
                if (queue.getStatus() == DownloadTaskQueue.DOWNLOAD) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<DownloadInfo> convertToDownload(ComicInfo info, List<Integer> position) {
        List<DownloadInfo> infoList = new ArrayList<>();
        for (int pos : position) {
            DownloadInfo downloadInfo = new DownloadInfo();
            ComicInfo.ChapterListBean bean = info.getChapter_list().get(pos);
            downloadInfo.setComic_id(info.getComic_id());
            downloadInfo.setChapter_name(bean.getChapter_title());
            downloadInfo.setChapter_url(bean.getChapter_url());
            downloadInfo.setComic_name(info.getComic_name());
            downloadInfo.setComic_url(info.getComic_url());
            downloadInfo.setCover(info.getCover());
            downloadInfo.setStatus(DownloadInfo.WAIT);
            long currentTimeMillis = System.currentTimeMillis();
            downloadInfo.setCreate_time((int) currentTimeMillis);
            infoList.add(downloadInfo);
        }
        return infoList;
    }

    @Override
    public void onQueueComplete(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueComplete(cid);
        }
        //检查是否还有其它队列,队列中是否有等待的任务,根据优先级开始队列的下载任务
        for (DownloadTaskQueue queue : queueList) {
            if (!queue.getCid().equals(cid) && queue.getStatus() == DownloadTaskQueue.WAIT) {
                queue.startQueue();
                Logger.d("开启" + queue.getCid() + "队列");
                return;
            }
        }
        Logger.d("已没有等待中的队列");
    }

    @Override
    public void onQueueWait(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueWait(cid);
        }
    }

    @Override
    public void onQueueStart(String cid, DownloadInfo info) {
        if (onQueueListener != null) {
            onQueueListener.onQueueStart(cid, info);
        }
    }

    @Override
    public void onQueuePause(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueuePause(cid);
        }
        //某一队列暂停之后,manager检测是否有等待中的队列
        for (DownloadTaskQueue queue : queueList) {
            if (queue.getStatus() == DownloadTaskQueue.WAIT) {
                queue.startQueue();
                break;
            }
        }
    }

    @Override
    public void onQueueResume(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueResume(cid);
        }
    }

    @Override
    public void onQueueStop(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueStop(cid);
        }
    }

    @Override
    public void onQueueFail(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueFail(cid);
        }
    }

    @Override
    public void onTaskWait(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskWait(info);
        }
    }

    @Override
    public void onTaskStart(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskStart(info);
        }
    }

    @Override
    public void onTaskPause(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskPause(info);
        }
    }

    @Override
    public void onTaskResume(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskResume(info);
        }
    }

    @Override
    public void onTaskStop(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskStop(info);
        }
    }

    @Override
    public void onTaskRestart(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskRestart(info);
        }
    }

    @Override
    public void onTaskComplete(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskComplete(info);
        }
    }

    @Override
    public void onTaskFail(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskFail(info);
        }
    }

    public class DownloadBinder extends IDownloadInterface.Stub {

        public DownloadManager getManager() {
            return DownloadManager.this;
        }

        @Override
        public void waitTask(DownloadInfo info) throws RemoteException {
            controlTask(info, DownloadTaskQueue.WAIT_TASK);
        }

        @Override
        public void startTask(DownloadInfo info) throws RemoteException {
            controlTask(info, DownloadTaskQueue.START_TASK);
        }

        @Override
        public void pauseTask(DownloadInfo info) throws RemoteException {
            controlTask(info, DownloadTaskQueue.PAUSE_TASK);
        }

        @Override
        public void resumeTask(DownloadInfo info) throws RemoteException {
            controlTask(info, DownloadTaskQueue.RESUME_TASK);
        }

        @Override
        public void stopTask(DownloadInfo info) throws RemoteException {
            controlTask(info, DownloadTaskQueue.STOP_TASK);
        }

        @Override
        public void restartTask(DownloadInfo info) throws RemoteException {
            controlTask(info, DownloadTaskQueue.RESTART_TASK);
        }

        @Override
        public void waitQueue(String cid) throws RemoteException {
            controlQueue(cid, WAIT_QUEUE);
        }

        @Override
        public void startQueue(String cid) throws RemoteException {
            controlQueue(cid, START_QUEUE);
        }

        @Override
        public void pauseQueue(String cid) throws RemoteException {
            controlQueue(cid, PAUSE_QUEUE);
        }

        @Override
        public void resumeQueue(String cid) throws RemoteException {
            controlQueue(cid, RESUME_QUEUE);
        }

        @Override
        public void stopQueue(String cid) throws RemoteException {
            controlQueue(cid, STOP_QUEUE);
        }

        private void controlQueue(String cid, int action) {
            if (queueList != null) {
                for (DownloadTaskQueue queue : queueList) {
                    if (queue.getCid().equals(cid)) {
                        switch (action) {
                            case WAIT_QUEUE:
                                queue.waitQueue();
                                break;
                            case START_QUEUE:
                                queue.startQueue();
                                break;
                            case PAUSE_QUEUE:
                                queue.pauseQueue();
                                break;
                            case RESUME_QUEUE:
                                queue.resumeQueue();
                                break;
                            case STOP_QUEUE:
                                queue.stopQueue();
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
            }
        }

        private void controlTask(DownloadInfo info, int action) {
            if (queueList != null) {
                for (DownloadTaskQueue queue : queueList) {
                    if (info.getComic_id().equals(queue.getCid())) {
                        switch (action) {
                            case DownloadTaskQueue.WAIT_TASK:
                                queue.waitTask(info);
                                break;
                            case DownloadTaskQueue.START_TASK:
                                queue.startTask(info);
                                break;
                            case DownloadTaskQueue.PAUSE_TASK:
                                queue.pauseTask(info);
                                break;
                            case DownloadTaskQueue.RESUME_TASK:
                                queue.resumeTask(info);
                                break;
                            case DownloadTaskQueue.RESTART_TASK:
                                queue.restartTask(info);
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * DownloadManagerActivity->DownloadManager的回调接口
     */
    public interface OnQueueListener {
        void onQueueComplete(String cid);

        void onQueueWait(String cid);

        void onQueueStart(String cid, DownloadInfo info);

        void onQueuePause(String cid);

        void onQueueResume(String cid);

        void onQueueStop(String cid);

        void onQueueFail(String cid);
    }

    public void setOnQueueListener(OnQueueListener onQueueListener) {
        this.onQueueListener = onQueueListener;
    }

    public void removeQueueListener() {
        this.onQueueListener = null;
    }

    /**
     * DownloadDetailActivity->DownloadManager的回调接口
     */
    public interface onTaskListener {
        void onTaskComplete(DownloadInfo info);

        void onTaskWait(DownloadInfo info);

        void onTaskStart(DownloadInfo info);

        void onTaskPause(DownloadInfo info);

        void onTaskResume(DownloadInfo info);

        void onTaskStop(DownloadInfo info);

        void onTaskRestart(DownloadInfo info);

        void onTaskFail(DownloadInfo info);
    }

    public void setOnTaskListener(onTaskListener onTaskListener) {
        this.onTaskListener = onTaskListener;
    }

    public void removeTaskListener() {
        this.onTaskListener = null;
    }
}
