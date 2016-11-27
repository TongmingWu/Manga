package com.tongming.manga.mvp.download;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.mvp.presenter.DownloadPresenterImp;
import com.tongming.manga.mvp.view.activity.DownloadDetailActivity;
import com.tongming.manga.mvp.view.activity.IDownloadView;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.server.IDownloadInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Author: Tongming
 * Date: 2016/9/9
 */

public class DownloadManager extends Service implements IDownloadManager, IDownloadView {

    public static final int WAIT_QUEUE = 0;
    public static final int START_QUEUE = 1;
    public static final int PAUSE_QUEUE = 2;
    public static final int RESUME_QUEUE = 3;
    public static final int STOP_QUEUE = 4;

    private int status;
    private DBManager manager;

    public static final int WAIT = 0x00010;
    public static final int DOWNLOAD = 0x00020;
    public static final int PAUSE = 0x00030;
    public static final int COMPLETE = 0x00040;
    public static final int STOP = 0x00050;

    public static final int START_TYPE_DETAIL = Integer.MIN_VALUE;

    private Set<DownloadTaskQueue> queueList;
    private DownloadBinder binder;
    private OnQueueListener onQueueListener;
    private onTaskListener onTaskListener;
    private DownloadPresenterImp downloadPresenterImp;
    private NotificationManager notificationManager;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new DownloadBinder();
        String cid = intent.getStringExtra("cid");
        DownloadInfo download = intent.getParcelableExtra("download");
        if (cid != null) {
            //查询下载信息
            queryDownloadInfoByCid(cid);
        } else if (download != null) {
            //根据单个downloadInfo建立下载队列
            ArrayList<DownloadInfo> list = new ArrayList<>();
            list.add(download);
            addQueue(download.getComic_id(), list);
        }
        if (manager == null) {
            manager = DBManager.getInstance();
        }
        return binder;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            ComicInfo info = intent.getParcelableExtra("info");
            List<Integer> pos = intent.getIntegerArrayListExtra("pos");
            if (info != null && pos != null) {
                String comicId = info.getComic_id();
                List<DownloadInfo> list = convertToDownload(info, pos);
                addQueue(comicId, list);
            }
        }
        if (manager == null) {
            manager = DBManager.getInstance();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void queryDownloadInfoByCid(String cid) {
        if (downloadPresenterImp == null) {
            downloadPresenterImp = new DownloadPresenterImp(this);
        }
        downloadPresenterImp.queryDownloadInfo(cid);
    }

    private void addQueue(String cid, List<DownloadInfo> list) {
        DownloadTaskQueue queue = checkQueue(cid);
        if (queue != null) {
            //已存在对应的下载队列
            Logger.d("追加任务");
            queue.addTask(list);
        } else {
            Logger.d("创建队列");
            queue = createQueue(cid, list);
        }
        //检测是否有下载中的队列,启动下载任务
        if (!checkQueuedStatus()) {
            queue.startQueue();
            status = DOWNLOAD;
        } else {
            if (queue.getStatus() != DownloadTaskQueue.DOWNLOAD) {
                queue.waitQueue();
            }
        }
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> infoList) {
        if (infoList.size() > 0) {
            Iterator<DownloadInfo> iterator = infoList.iterator();
            while (iterator.hasNext()) {
                DownloadInfo info = iterator.next();
                if (info.getStatus() == DownloadInfo.COMPLETE) {
                    iterator.remove();
                }
                info.setStatus(DownloadInfo.WAIT);
            }
            addQueue(infoList.get(0).getComic_id(), infoList);
        }
    }

    @Override
    public void onQueryDownloadInfo(ComicPage page) {

    }

    @Override
    public void onDeleteDownloadInfo(int state) {

    }

    public DownloadTaskQueue createQueue(String cid, List<DownloadInfo> infoList) {
        DownloadTaskQueue queue = new DownloadTaskQueue(this, cid, infoList);
        queueList.add(queue);
        return queue;
    }

    /**
     * 检查队列是否存在
     *
     * @param cid 漫画id
     */
    public DownloadTaskQueue checkQueue(String cid) {
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

    /**
     * 检查指定cid的队列是否在下载
     *
     * @param cid comic_id
     */
    public boolean checkQueueStatus(String cid) {
        if (queueList != null) {
            for (DownloadTaskQueue queue : queueList) {
                if (cid.equals(queue.getCid()) && queue.getStatus() == DownloadTaskQueue.DOWNLOAD) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查是否有队列在下载
     */
    public boolean checkQueuedStatus() {
        if (queueList != null) {
            for (DownloadTaskQueue queue : queueList) {
                if (queue.getStatus() == DownloadTaskQueue.DOWNLOAD) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 在通知栏上显示下载信息
     */
    private void sendNotification(DownloadInfo info, int flag) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.notice_download);
        rv.setProgressBar(R.id.pb_notice, info.getTotal(), info.getPosition(), false);
        rv.setTextViewText(R.id.tv_notice_desc, "当前下载:" + info.getChapter_name());
        rv.setTextColor(R.id.tv_notice_desc, getColor(R.color.normalText));
        rv.setTextViewText(R.id.tv_progress, (int) (((float) info.getPosition() / (float) info.getTotal()) * 100) + "%");
        rv.setTextColor(R.id.tv_progress, getColor(R.color.normalText));
        Intent intent = new Intent(this, DownloadDetailActivity.class);
        intent.putExtra("cid", info.getComic_id());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setContent(rv);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        notificationManager.notify(R.layout.notice_download, builder.build());
    }

    /**
     * 转换DownloadInfo
     */
    private List<DownloadInfo> convertToDownload(ComicInfo info, List<Integer> position) {
        List<DownloadInfo> infoList = new ArrayList<>();
        List<ComicInfo.ChapterListBean> chapterList = info.getChapter_list();
        int size = chapterList.size();
        int next;
        int prepare;
        String nextUrl;
        String preUrl;
        for (int pos : position) {
            DownloadInfo downloadInfo = new DownloadInfo();
            ComicInfo.ChapterListBean bean = chapterList.get(pos);
            downloadInfo.setComic_id(info.getComic_id());
            downloadInfo.setChapter_name(bean.getChapter_title());
            downloadInfo.setChapter_url(bean.getChapter_url());
            downloadInfo.setComic_name(info.getComic_name());
            downloadInfo.setComic_source(info.getComic_source());
            downloadInfo.setComic_url(info.getComic_url());
            downloadInfo.setCover(info.getCover());
            downloadInfo.setStatus(DownloadInfo.WAIT);
            long currentTimeMillis = System.currentTimeMillis();
            downloadInfo.setCreate_time((int) currentTimeMillis);
            next = (pos > 0 && pos < size - 1) ? 1 : (pos == 0 ? 0 : 1);
            nextUrl = next > 0 ? chapterList.get(pos - 1).getChapter_url() : "";
            prepare = (pos > 0 && pos < size - 1) ? 1 : (pos == size - 1 ? 0 : 1);
            preUrl = prepare > 0 ? chapterList.get(pos + 1).getChapter_url() : "";
            downloadInfo.setNext(next);
            downloadInfo.setPrepare(prepare);
            downloadInfo.setNext_url(nextUrl);
            downloadInfo.setPre_url(preUrl);
            infoList.add(downloadInfo);
        }
        return infoList;
    }

    @Override
    public void onQueueComplete(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueComplete(cid);
        }
        //移除已下载完成的Queue
        //检查是否还有其它队列,队列中是否有等待的任务,根据优先级开始队列的下载任务
        for (DownloadTaskQueue queue : queueList) {
            if (!queue.getCid().equals(cid) && queue.getStatus() == DownloadTaskQueue.WAIT) {
                queue.startQueue();
                Logger.d("开启" + queue.getCid() + "队列");
                return;
            }
        }
        status = COMPLETE;
        notificationManager.cancel(R.layout.notice_download);
        stopSelf();
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
        status = DOWNLOAD;
    }

    @Override
    public void onQueuePause(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueuePause(cid);
        }
        status = PAUSE;
        int pauseSize = 0;
        //某一队列暂停之后,manager检测是否有等待中的队列
        for (DownloadTaskQueue queue : queueList) {
            switch (queue.getStatus()) {
                case DownloadTaskQueue.PAUSE:
                    pauseSize++;
                    break;
                case DownloadTaskQueue.WAIT:
                    if (status != DOWNLOAD) {
                        queue.startQueue();
                        status = DOWNLOAD;
                    }
                    break;
            }
        }
        if (pauseSize == queueList.size()) {
            status = PAUSE;
            notificationManager.cancel(R.layout.notice_download);
            stopSelf();
        }
    }

    @Override
    public void onQueueResume(String cid) {
        if (onQueueListener != null) {
            onQueueListener.onQueueResume(cid);
        }
        status = DOWNLOAD;
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
        sendNotification(info, info.getStatus());
    }

    @Override
    public void onTaskPause(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskPause(info);
        }
        notificationManager.cancel(R.layout.notice_download);
    }

    @Override
    public void onTaskResume(DownloadInfo info) {
        if (onTaskListener != null) {
            onTaskListener.onTaskResume(info);
        }
        sendNotification(info, info.getStatus());
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

    @Override
    public void onFail(Throwable throwable) {

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
                //先判断Queue是否存在,不存在则添加
                for (DownloadTaskQueue queue : queueList) {
                    if (queue.getCid().equals(cid)) {
                        switch (action) {
                            case WAIT_QUEUE:
                                queue.waitQueue();
                                break;
                            case START_QUEUE:
                                if (status != DOWNLOAD) {
                                    queue.startQueue();
                                    status = DOWNLOAD;
                                } else {
                                    queue.waitQueue();
                                }
                                break;
                            case PAUSE_QUEUE:
                                queue.pauseQueue();
                                break;
                            case RESUME_QUEUE:
                                if (status != DOWNLOAD) {
                                    //...
                                    queue.resumeQueue();
                                    status = DOWNLOAD;
                                } else {
                                    queue.waitQueue();
                                }
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
                //检测task所在的Queue是否存在,不存在则添加
                boolean isQueueExist = false;
                for (DownloadTaskQueue queue : queueList) {
                    if (info.getComic_id().equals(queue.getCid())) {
                        isQueueExist = true;
                        switch (action) {
                            case DownloadTaskQueue.WAIT_TASK:
                                queue.waitTask(info);
                                break;
                            case DownloadTaskQueue.START_TASK:
                                if (status != DOWNLOAD) {
                                    queue.startTask(info);
                                    status = DOWNLOAD;
                                } else {
                                    queue.waitQueue();
                                }
                                break;
                            case DownloadTaskQueue.PAUSE_TASK:
                                queue.pauseTask(info);
                                break;
                            case DownloadTaskQueue.RESUME_TASK:
                                //检测task所在的Queue是否存在,不存在则添加
                                //并且是否在Queue中,不存在则添加
                                for (DownloadTaskQueue q : queueList) {
                                    if (q.getCid().equals(info.getComic_id())) {
                                        isQueueExist = true;
                                    }
                                }
                                boolean isExist = false;
                                for (DownloadTask task : queue.getTaskList()) {
                                    if (task.hashCode() == info.hashCode()) {
                                        isExist = true;
                                    }
                                }
                                if (!isExist) {
                                    ArrayList<DownloadInfo> list = new ArrayList<>();
                                    list.add(info);
                                    queue.addTask(list);
                                }
                                if (status != DOWNLOAD) {
                                    queue.resumeTask(info);
                                    status = DOWNLOAD;
                                } else {
                                    queue.waitTask(info);
                                }
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
                if (!isQueueExist) {
                    //Queue不存在的情况
                    Logger.d("Queue不存在");
                    ArrayList<DownloadInfo> list = new ArrayList<>();
                    list.add(info);
                    DownloadTaskQueue taskQueue = new DownloadTaskQueue(DownloadManager.this, info.getComic_id(), list);
                    queueList.add(taskQueue);
                    if (status != DOWNLOAD) {
                        taskQueue.resumeTask(info);
                        status = DOWNLOAD;
                    } else {
                        taskQueue.waitTask(info);
                    }
                }
            }
        }
    }

    @Override
    public DBManager getDBManager() {
        return manager == null ? DBManager.getInstance() : manager;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
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
