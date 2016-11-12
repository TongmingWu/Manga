package com.tongming.manga.mvp.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.CusGridView;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.download.DownloadManager;
import com.tongming.manga.mvp.presenter.DownloadPresenterImp;
import com.tongming.manga.mvp.view.adapter.ChapterAdapter;
import com.tongming.manga.server.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public class SelectActivity extends SwipeBackActivity implements IDownloadView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_chapter_num)
    TextView tvChapterNum;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.gv_chapter)
    CusGridView gvChapter;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout rlBottomBar;
    private ComicInfo info;
    private ChapterAdapter adapter;
    private int selectionNum;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select;
    }

    /**
     * toolbar标题为漫画名
     * 页面显示内容为comic_name,status,date,chapter_list
     * 检测每一话,是否为已下载,已下载的item变色并且不可点击
     * 点击未下载item,多选
     * 1个查询是否已下载的接口
     */
    @Override
    protected void initView() {
        initToolbar(toolbar);
        info = getIntent().getParcelableExtra("info");
        toolbar.setTitle(info.getComic_name());
        tvChapterNum.setText("共" + info.getChapter_list().size() + "话");
        tvDate.setText("更新于" + info.getNewest_chapter_date().split(" ")[0]);
        presenter = new DownloadPresenterImp(this);
        ((DownloadPresenterImp) presenter).queryDownloadInfo(info.getComic_id());
    }

    @OnClick({R.id.tv_select_all, R.id.tv_download})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_all:
                selectAll();
                break;
            case R.id.tv_download:
                if (selectionNum > 0) {
                    download();
                }
                break;
        }
    }

    private void selectAll() {
        if (tvSelectAll.getText().toString().equals("全选")) {
            tvSelectAll.setText("取消全选");
            tvSelectAll.setBackgroundResource(R.drawable.gray_border);
            tvSelectAll.setTextColor(Color.LTGRAY);
            tvSelect.setText("已选择" + info.getChapter_list().size() + "项");
            tvDownload.setBackgroundResource(R.drawable.download_bg);
            selectionNum = adapter.selectAll();
        } else {
            tvSelectAll.setText("全选");
            tvSelectAll.setBackgroundResource(R.drawable.btn_primary_color_border);
            tvSelectAll.setTextColor(getResources().getColor(R.color.colorPrimary, null));
            tvSelect.setText("选择需要下载的章节");
            tvDownload.setBackgroundResource(R.drawable.btn_undownload_bg);
            selectionNum = adapter.unselectAll();
        }
        adapter.notifyDataSetChanged();
    }

    private void download() {
        List<Integer> downloadPos = adapter.getSelectionPos();
        //添加下载任务
        Intent intent = new Intent(this, DownloadManager.class);
        intent.putExtra("info", info);
        intent.putIntegerArrayListExtra("pos", (ArrayList<Integer>) downloadPos);
        startService(intent);
        Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> infoList) {
        List<Integer> downloadPos = new ArrayList<>();
        if (infoList.size() > 0) {
            for (int i = 0; i < infoList.size(); i++) {
                for (int j = 0; j < info.getChapter_list().size(); j++) {
                    ComicInfo.ChapterListBean bean = info.getChapter_list().get(j);
                    if (bean.getChapter_title().equals(infoList.get(i).getChapter_name())) {
                        downloadPos.add(j);
                        break;
                    }
                }
            }
        }
        adapter = new ChapterAdapter(info.getChapter_list(), downloadPos, this);
        gvChapter.setAdapter(adapter);
        gvChapter.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //改变颜色
                selectionNum = adapter.addSelectPos(i);
                if (selectionNum > 0) {
                    tvSelect.setText("已选择" + selectionNum + "项");
                    tvDownload.setBackgroundResource(R.drawable.download_bg);
                } else {
                    tvSelect.setText("选择需要下载的章节");
                    tvDownload.setBackgroundResource(R.drawable.btn_undownload_bg);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onQueryDownloadInfo(ComicPage page) {

    }

    @Override
    public void onDeleteDownloadInfo(int state) {

    }


    @Override
    public void onFail(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.closeDB();
        }
    }
}
