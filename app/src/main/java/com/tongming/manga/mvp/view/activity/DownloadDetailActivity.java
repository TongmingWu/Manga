package com.tongming.manga.mvp.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.download.DownloadManager;
import com.tongming.manga.mvp.presenter.DownloadPresenterImp;
import com.tongming.manga.mvp.view.adapter.DownloadAdapter;
import com.tongming.manga.server.DownloadConnection;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.util.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public class DownloadDetailActivity extends BaseActivity implements IQueryDownloadView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_download)
    RecyclerView rvDownload;
    private DownloadManager manager;
    private DownloadConnection conn;
    private DownloadAdapter adapter;
    private List<DownloadInfo> infoList;
    private DownloadManager.DownloadBinder binder;
    private boolean serviceStarted;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download_detail;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        new DownloadPresenterImp(this).queryDownloadInfo(this, cid);
        serviceStarted = CommonUtil.isServiceStarted(this, DownloadManager.class.getName());
        if (serviceStarted) {
            //绑定Service
            if (conn == null) {
                conn = new DownloadConnection(this, DownloadConnection.DOWNLOAD_TASK);
            }
            bindService(new Intent(this, DownloadManager.class), conn, Context.BIND_AUTO_CREATE);
        } else {
            Logger.d("manager未启动");
        }
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> list) {
        infoList = list;
        if (adapter == null) {
            adapter = new DownloadAdapter(infoList, this);
            rvDownload.setLayoutManager(new LinearLayoutManager(this));
            rvDownload.addItemDecoration(new SpaceItemDecoration(10));
            adapter.setOnItemClickListener(new DownloadAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //开始,暂停,观看
                    DownloadInfo info = infoList.get(position);
                    int status = info.getStatus();
                    switch (status) {
                        case DownloadInfo.DOWNLOAD:
                            if (serviceStarted) {
                                try {
                                    Logger.d("暂停" + info.getChapter_name() + "的下载");
                                    binder.pauseTask(info);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case DownloadInfo.PAUSE:
                            if (serviceStarted) {
                                try {
                                    Logger.d("继续" + info.getChapter_name() + "的下载");
                                    binder.resumeTask(info);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case DownloadInfo.COMPLETE:
                            //跳转到PageActivity
                            break;
                    }
                }
            });
            adapter.setOnItemLongClickListener(new DownloadAdapter.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(View view, int position) {
                    return false;
                }
            });
            rvDownload.setAdapter(adapter);
        }

    }

    public void setOnTaskListener() {
        binder = conn.getBinder();
        conn.getManager().setOnTaskListener(new DownloadManager.onTaskListener() {
            @Override
            public void onTaskComplete(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务完成");
            }

            @Override
            public void onTaskWait(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务等待");
            }

            @Override
            public void onTaskStart(DownloadInfo info) {
                notifyItemChanged(info);
            }

            @Override
            public void onTaskPause(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务暂停");
            }

            @Override
            public void onTaskResume(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务继续");
            }

            @Override
            public void onTaskStop(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务取消");
            }

            @Override
            public void onTaskRestart(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务重启");
            }

            @Override
            public void onTaskFail(DownloadInfo info) {
                notifyItemChanged(info);
                Logger.d(info.getChapter_name() + "任务失败");
            }
        });
    }

    private void notifyItemChanged(DownloadInfo info) {
        for (int index = 0; index < infoList.size(); index++) {
            DownloadInfo downloadInfo = infoList.get(index);
            downloadInfo.setStatus(info.getStatus());
            downloadInfo.setPosition(info.getPosition());
            downloadInfo.setTotal(info.getTotal());
            if (info.getChapter_url().equals(downloadInfo.getChapter_url())) {
                if (info.getStatus() == DownloadInfo.DOWNLOAD) {
                    DownloadAdapter.DownloadHolder holder = (DownloadAdapter.DownloadHolder) rvDownload.findViewHolderForAdapterPosition(index);
                    holder.updateItem(downloadInfo);
                } else {
                    adapter.notifyItemChanged(index);
                }
            }
        }
    }

    @Override
    public void onFail(Throwable throwable) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            conn.getManager().removeTaskListener();
            unbindService(conn);
        }
    }
}
