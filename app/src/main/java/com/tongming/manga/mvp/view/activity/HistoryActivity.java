package com.tongming.manga.mvp.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.presenter.HistoryPresenterImp;
import com.tongming.manga.mvp.view.adapter.ComicAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryActivity extends BaseActivity implements IHistoryView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.gv_history)
    GridView gvHistory;
    @BindView(R.id.fab_clear)
    FloatingActionButton fabClear;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void initView() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, 80, 0, 0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (presenter == null) {
            presenter = new HistoryPresenterImp(this);
        }
        ((HistoryPresenterImp) presenter).queryAllHistory(this);
        fabClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("注意")
                        .setMessage("确定清空历史记录?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((HistoryPresenterImp) presenter).deleteAllHistory(HistoryActivity.this);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
    }

    @Override
    public void onQuery(final List<HistoryComic> comics) {
        Logger.d("查询历史记录");
        gvHistory.setAdapter(new ComicAdapter(comics, this, ComicAdapter.HISTORY_COMIC));
        gvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
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
