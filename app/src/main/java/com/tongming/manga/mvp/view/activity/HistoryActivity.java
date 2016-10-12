package com.tongming.manga.mvp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.presenter.HistoryPresenterImp;
import com.tongming.manga.mvp.view.adapter.RVComicAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryActivity extends BaseActivity implements IHistoryView {
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
                                    ((HistoryPresenterImp) presenter).deleteAllHistory(HistoryActivity.this);
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        /*gvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, ComicDetailActivity.class);
                HistoryComic comic = comics.get(position);
                String name = comic.getName();
                intent.putExtra("url", comic.getUrl()).putExtra("name", name.endsWith("漫画") ? name.replace("漫画", "") : name);
                startActivity(intent);
            }
        });
        gvHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("注意")
                        .setMessage("确定要删除" + comics.get(position).getName() + "吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((HistoryPresenterImp) presenter).deleteHistoryByName(HistoryActivity.this, comics.get(position).getName());
                            }
                        }).setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });*/

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
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HistoryActivity.this, ComicDetailActivity.class);
                HistoryComic comic = comics.get(position);
                String name = comic.getName();
                intent.putExtra("url", comic.getUrl())
                        .putExtra("name", name.endsWith("漫画") ? name.replace("漫画", "") : name)
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
            public boolean onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("注意")
                        .setMessage("确定要删除" + comics.get(position).getName() + "吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((HistoryPresenterImp) presenter).deleteHistoryByName(HistoryActivity.this, comics.get(position).getName());
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
        ((HistoryPresenterImp) presenter).queryAllHistory(this);
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
    public void onDeleteByName(int state) {
        ((HistoryPresenterImp) presenter).queryAllHistory(this);
        Logger.d("删除指定历史");
    }

    @Override
    public void onDeleteAll(int state) {
        Logger.d("清空历史记录");
        ((HistoryPresenterImp) presenter).queryAllHistory(this);
    }
}
