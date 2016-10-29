package com.tongming.manga.server;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.download.DownloadManager;
import com.tongming.manga.mvp.view.activity.DownloadDetailActivity;
import com.tongming.manga.mvp.view.activity.DownloadManagerActivity;

/**
 * Author: Tongming
 * Date: 2016/9/18
 */

public class DownloadConnection implements ServiceConnection {

    private DownloadManager manager;
    private DownloadManager.DownloadBinder binder;
    public static final int DOWNLOAD_QUEUE = 0x2143;
    public static final int DOWNLOAD_TASK = 0x2144;
    private int flag;
    private Context context;
    private OnConnectedListener onConnectedListener;

    public DownloadConnection(Context context, int flag) {
        this.context = context;
        this.flag = flag;
    }

    public DownloadConnection(Context context, int flag, OnConnectedListener onConnectedListener) {
        this.context = context;
        this.flag = flag;
        this.onConnectedListener = onConnectedListener;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (DownloadManager.DownloadBinder) service;
        manager = binder.getManager();
        switch (flag) {
            case DOWNLOAD_QUEUE:
                DownloadManagerActivity managerActivity = (DownloadManagerActivity) context;
                managerActivity.setQueueListener();
                break;
            case DOWNLOAD_TASK:
                DownloadDetailActivity detailActivity = (DownloadDetailActivity) context;
                detailActivity.setOnTaskListener();
                if (onConnectedListener != null) {
                    onConnectedListener.onServiceConnected();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Logger.d("断开连接");
    }

    public DownloadManager getManager() {
        return manager;
    }

    public DownloadManager.DownloadBinder getBinder() {
        return binder;
    }

    public interface OnConnectedListener {
        void onServiceConnected();
    }
}
