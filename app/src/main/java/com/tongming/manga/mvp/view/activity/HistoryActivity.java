package com.tongming.manga.mvp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.presenter.HistoryPresenterImp;
import com.tongming.manga.mvp.view.adapter.RVComicAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryActivity extends SwipeBackActivity implements IHistoryView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //    @BindView(R.id.gv_history)
//    GridView gvHistory;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.fab_clear)
    FloatingActionButton fabClear;
    private List<HistoryComic> comics;
    private RVComicAdapter adapter;
    private HistoryComic deleteComic;
    private int deletePos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("注意")
                        .setMessage("确定清空历史记录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (presenter != null) {
                                    ((HistoryPresenterImp) presenter).deleteAllHistory();
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    private void initRecyclerView() {
        rvHistory.setLayoutManager(new GridLayoutManager(this, 3));
        rvHistory.addItemDecoration(new SpaceItemDecoration(CommonUtil.dip2px(this, 10), true));
        if (adapter == null) {
            adapter = new RVComicAdapter(comics, this, RVComicAdapter.HISTORY_COMIC);
        }
        rvHistory.setAdapter(adapter);
        adapter.setOnItemClickListener(new RVComicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                HistoryComic comic = (HistoryComic) object;
                Intent intent = new Intent(HistoryActivity.this, ComicDetailActivity.class);
                String name = comic.getName();
                intent.putExtra("url", comic.getUrl())
                        .putExtra("name", name.endsWith("漫画") ? name.replace("漫画", "") : name)
                        .putExtra("source", comic.getComic_source())
                        .putExtra("cover", comic.getCover());
                if (Build.VERSION.SDK_INT >= 20) {
                    ImageView ivCover = (ImageView) view.findViewById(R.id.iv_cover);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(HistoryActivity.this, ivCover, getString(R.string.coverName));
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        adapter.setOnItemLongClickListener(new RVComicAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position, Object object) {
                final HistoryComic comic = (HistoryComic) object;
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("注意")
                        .setMessage("确定要删除" + comic.getName() + "吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteComic = comic;
                                for (int pos = 0; pos < comics.size(); pos++) {
                                    if (comics.get(pos).equals(comic.getName())) {
                                        deletePos = pos;
                                        break;
                                    }
                                }
                                ((HistoryPresenterImp) presenter).deleteHistoryByName(deleteComic.getName());
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryHistory();
    }

    private void queryHistory() {
        if (presenter == null) {
            presenter = new HistoryPresenterImp(this);
        }
        ((HistoryPresenterImp) presenter).queryAllHistory();
    }

    @Override
    public void onQuery(List<HistoryComic> comics) {
        if (this.comics == null) {
            this.comics = comics;
            initRecyclerView();
        } else {
            this.comics.clear();
            this.comics.addAll(comics);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDeleteByName(int state, String name) {
        for (int index = 0; index < comics.size(); index++) {
            if (comics.get(index).getName().equals(name)) {
                comics.remove(index);
                adapter.notifyItemRemoved(index);
                break;
            }
        }
        Snackbar snackbar = Snackbar.make(rvHistory, "删除成功", Snackbar.LENGTH_SHORT);
        snackbar.setAction("撤销", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在删除之前保存信息,点击撤销恢复信息
                ((HistoryPresenterImp) presenter).restoreHistory(deleteComic);
            }
        });
        snackbar.show();
    }

    @Override
    public void onRestoreHistory(int state) {
        if (state > 0) {
            comics.add(deletePos, deleteComic);
            adapter.notifyItemInserted(deletePos);
        }
    }

    @Override
    public void onDeleteAll(int state) {
        comics.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.closeDB();
        }
    }
}
