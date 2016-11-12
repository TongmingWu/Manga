package com.tongming.manga.mvp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.DownloadComic;
import com.tongming.manga.mvp.download.DownloadManager;
import com.tongming.manga.mvp.download.DownloadTaskQueue;
import com.tongming.manga.mvp.presenter.DownloadPresenterImp;
import com.tongming.manga.mvp.view.adapter.ComicAdapter;
import com.tongming.manga.mvp.view.adapter.RVComicAdapter;
import com.tongming.manga.server.DownloadConnection;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.util.CommonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public class DownloadManagerActivity extends SwipeBackActivity implements IDownloadView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_download)
    RecyclerView rvDownload;
    AlertDialog dialog;
    private DownloadConnection conn;
    private RVComicAdapter adapter;
    private List<DownloadComic> comicList;
    private boolean serviceStarted;
    private DownloadManager.DownloadBinder binder;
    private List<DownloadInfo> infoList;
    private DownloadPresenterImp imp;
    private int deletePos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download_manager;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (imp == null) {
            imp = new DownloadPresenterImp(this);
        }
        imp.queryAllDownloadInfo();
        serviceStarted = CommonUtil.isServiceStarted(this, DownloadManager.class.getName());
        if (serviceStarted && conn == null) {
            //绑定Service
            Intent intent = new Intent(this, DownloadManager.class);
            conn = new DownloadConnection(this, DownloadConnection.DOWNLOAD_QUEUE);
            bindService(intent, conn, BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> infoList) {
        this.infoList = infoList;
        calculateComic(this.infoList);
        Collections.sort(comicList);
        initRecyclerView();
    }

    @Override
    public void onQueryDownloadInfo(ComicPage page) {

    }

    private void initRecyclerView() {
        if (adapter == null) {
            adapter = new RVComicAdapter(comicList, this, ComicAdapter.DOWNLOAD_COMIC);
            rvDownload.setLayoutManager(new GridLayoutManager(this, 3));
            rvDownload.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(this, 10), true));
            rvDownload.setAdapter(adapter);
            adapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, Object object) {
                    jumpToDetail(comicList, position);
                }
            });
            adapter.setOnItemLongClickListener(new RVComicAdapter.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(View view, final int position, Object object) {
                    //删除,暂停,进入队列功能
                    View inflate = View.inflate(DownloadManagerActivity.this, R.layout.dialog_download, null);
                    TextView tvManager = (TextView) inflate.findViewById(R.id.tv_manager);
                    tvManager.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpToDetail(comicList, position);
                            dialog.dismiss();
                        }
                    });
                    TextView tvStart = (TextView) inflate.findViewById(R.id.tv_start);
                    final int status = comicList.get(position).getStatus();
                    switch (status) {
                        case DownloadTaskQueue.DOWNLOAD:
                        case DownloadTaskQueue.WAIT:
                            tvStart.setVisibility(View.VISIBLE);
                            tvStart.setText("暂停");
                            break;
                        case DownloadTaskQueue.PAUSE:
                            tvStart.setVisibility(View.VISIBLE);
                            break;
                        case DownloadTaskQueue.COMPLETE:
                            break;

                    }
                    tvStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String cid = comicList.get(position).getComic_id();
                            if (status == DownloadTaskQueue.DOWNLOAD || status == DownloadTaskQueue.WAIT) {
                                //暂停队列
                                try {
                                    binder.pauseQueue(cid);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //继续队列
                                if (serviceStarted) {
                                    Logger.d("继续队列");
                                    try {
                                        DownloadManager manager = binder.getManager();
                                        if (manager.checkQueue(cid) == null) {
                                            //队列不存在,新建队列
                                            ArrayList<DownloadInfo> list = new ArrayList<>();
                                            for (DownloadInfo info : infoList) {
                                                if (info.getComic_id().equals(cid)) {
                                                    list.add(info);
                                                }
                                            }
                                            DownloadTaskQueue queue = manager.createQueue(cid, list);
                                            if (manager.checkQueuedStatus()) {
                                                queue.waitQueue();
                                            } else {
                                                queue.startQueue();
                                                manager.setStatus(DownloadManager.DOWNLOAD);
                                            }
                                        } else {
                                            binder.resumeQueue(cid);
                                        }
                                    } catch (RemoteException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    //开启service并绑定
                                    Intent intent = new Intent(DownloadManagerActivity.this, DownloadManager.class);
                                    intent.putExtra("cid", cid);
                                    startService(intent);
                                    serviceStarted = true;
                                    if (conn == null) {
                                        conn = new DownloadConnection(DownloadManagerActivity.this, DownloadConnection.DOWNLOAD_QUEUE);
                                        bindService(intent, conn, BIND_AUTO_CREATE);
                                    }
                                }
                            }
                            dialog.dismiss();
                        }
                    });

                    TextView tvDelete = (TextView) inflate.findViewById(R.id.tv_delete);
                    tvDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //删除包括已下载的漫画和数据库的记录
                            //1.service已启动并queue开始;2.service已启动但Queue未开始;
                            //3.service未启动
                            new AlertDialog.Builder(DownloadManagerActivity.this)
                                    .setTitle("注意")
                                    .setMessage("确定删除" + comicList.get(position).getName() + "?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String cid = comicList.get(position).getComic_id();
                                            if (serviceStarted) {
                                                //先暂停
                                                DownloadTaskQueue queue = binder.getManager().checkQueue(cid);
                                                if (queue != null) {
                                                    queue.pauseQueue();
                                                }
                                            }
                                            deletePos = position;
                                            deleteComic(cid);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                            dialog.dismiss();
                        }
                    });
                    dialog = new AlertDialog.Builder(DownloadManagerActivity.this)
                            .setTitle(comicList.get(position).getName())
                            .setView(inflate)
                            .setNegativeButton("取消", null)
                            .show();
                    return true;
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除已下载的漫画
     */
    private void deleteComic(String cid) {
        imp.deleteDownloadInfo(cid);
    }

    @Override
    public void onDeleteDownloadInfo(int state) {
        if (state > 0) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            comicList.remove(deletePos);
            adapter.notifyItemRemoved(deletePos);
        }
    }

    public void setQueueListener() {
        binder = conn.getBinder();
        //Manager回调,刷新页面
        conn.getManager().setOnQueueListener(new DownloadManager.OnQueueListener() {
            @Override
            public void onQueueComplete(String cid) {
                if (adapter != null) {
                    notifyItemChanged(cid, DownloadTaskQueue.COMPLETE, null);
                    Logger.d(cid + "暂停下载");
                }
            }

            @Override
            public void onQueueWait(String cid) {
                if (adapter != null) {
                    notifyItemChanged(cid, DownloadTaskQueue.WAIT, null);
                }
            }

            @Override
            public void onQueueStart(String cid, DownloadInfo info) {
                if (adapter != null) {
                    notifyItemChanged(cid, DownloadTaskQueue.DOWNLOAD, info);
                }
            }

            @Override
            public void onQueuePause(String cid) {
                if (adapter != null) {
                    notifyItemChanged(cid, DownloadTaskQueue.PAUSE, null);
                }
            }

            @Override
            public void onQueueResume(String cid) {
                if (adapter != null) {
                    notifyItemChanged(cid, DownloadTaskQueue.DOWNLOAD, null);
                }
            }

            @Override
            public void onQueueStop(String cid) {
                if (adapter != null) {
//                    adapter.removeQueue(cid);
                    notifyItemChanged(cid, DownloadTaskQueue.STOP, null);
                }
            }

            @Override
            public void onQueueFail(String cid) {
                Logger.d(cid + "下载失败");
            }
        });
    }

    private void notifyItemChanged(String cid, int action, DownloadInfo info) {
        for (int index = 0; index < comicList.size(); index++) {
            DownloadComic comic = comicList.get(index);
            if (cid.equals(comic.getComic_id())) {
//                adapter.notifyItemChanged(i);
                comic.setStatus(action);
                if (action == DownloadTaskQueue.DOWNLOAD && info != null) {
                    comic.setCurrentTotal(info.getTotal());
                    comic.setCurrentPosition(info.getPosition());
                    comic.setCurrentName(info.getChapter_name());
                    RVComicAdapter.ViewHolder holder = (RVComicAdapter.ViewHolder) rvDownload.findViewHolderForAdapterPosition(index);
                    holder.updateItem(info);
                } else if (action == DownloadTaskQueue.STOP) {
                    comicList.remove(index);
                    adapter.notifyItemRemoved(index);
                } else {
                    adapter.notifyItemChanged(index);
                }
                break;
            }
        }
    }

    private void jumpToDetail(List<DownloadComic> comicList, int position) {
        Intent intent = new Intent(DownloadManagerActivity.this, DownloadDetailActivity.class);
        intent.putExtra("cid", comicList.get(position).getComic_id());
        startActivity(intent);
    }

    private void calculateComic(List<DownloadInfo> infoList) {
        if (comicList == null) {
            comicList = new ArrayList<>();
        } else {
            comicList.clear();
        }
        for (DownloadInfo info : infoList) {
            boolean exitCid = false;
            for (DownloadComic comic : comicList) {
                if (comic.getComic_id().equals(info.getComic_id())) {
                    exitCid = true;
                    break;
                }
            }
            if (!exitCid) {
                DownloadComic comic = new DownloadComic();
                comic.setComic_id(info.getComic_id());
                comic.setCover(info.getCover());
                comic.setComic_source(info.getComic_source());
                comic.setName(info.getComic_name());
                comicList.add(comic);
            }
            //检测任务的状态:下载中,等待中,暂停中,下载完成 (优先级从上到下)
            for (DownloadComic comic : comicList) {
                if (comic.getComic_id().equals(info.getComic_id())) {
                    comic.addCount();
                    comic.setCreateTime(info.getCreate_time() > comic.getCreateTime() ? info.getCreate_time() : comic.getCreateTime());
                    int status = comic.getStatus();
                    switch (info.getStatus()) {
                        case DownloadInfo.DOWNLOAD:
                            comic.setStatus(DownloadTaskQueue.DOWNLOAD);
                            comic.setCurrentName(info.getChapter_name());
                            comic.setCurrentPosition(info.getPosition());
                            comic.setCurrentTotal(info.getTotal());
                            break;
                        case DownloadInfo.WAIT:
                            if (status != DownloadTaskQueue.DOWNLOAD) {
                                comic.setStatus(DownloadTaskQueue.WAIT);
                            }
                            break;
                        case DownloadInfo.PAUSE:
                            if (status != DownloadTaskQueue.DOWNLOAD && status != DownloadTaskQueue.WAIT) {
                                comic.setStatus(DownloadTaskQueue.PAUSE);
                            }
                            break;
                        case DownloadInfo.COMPLETE:
                            if (status != DownloadTaskQueue.DOWNLOAD && status != DownloadTaskQueue.WAIT && status != DownloadTaskQueue.PAUSE) {
                                comic.setStatus(DownloadTaskQueue.COMPLETE);
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            conn.getManager().removeQueueListener();
            unbindService(conn);
        }
        if (presenter != null) {
            presenter.closeDB();
        }
    }
}
