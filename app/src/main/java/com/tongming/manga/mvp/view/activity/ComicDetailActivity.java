package com.tongming.manga.mvp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.presenter.DetailPresenterImp;
import com.tongming.manga.mvp.presenter.DownloadPresenterImp;
import com.tongming.manga.mvp.view.adapter.ChapterAdapter;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.util.CommonUtil;
import com.tongming.manga.util.HeaderGlide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Retrofit;

/**
 * Created by Tongming on 2016/8/10.
 */
public class ComicDetailActivity extends SwipeBackActivity implements IDetailView, IDownloadView {
    public static final int REQUEST_CHAPTER_CODE = 0x15;
    public static final int REQUEST_DOWNLOAD_CODE = 0x8856;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.iv_blur)
    ImageView ivBlur;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.tv_comic_name)
    TextView tvComicName;
    @BindView(R.id.tv_comic_author)
    TextView tvComicAuthor;
    @BindView(R.id.tv_comic_status)
    TextView tvComicStatus;
    @BindView(R.id.tv_comic_type)
    TextView tvComicType;
    @BindView(R.id.tv_comic_desc)
    TextView tvDesc;
    /*@BindView(R.id.iv_fav)
    ImageView ivFav;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    @BindView(R.id.iv_share)
    ImageView ivShare;*/
    /*@BindView(R.id.iv_sort)
    ImageView ivSort;*/
    @BindView(R.id.btn_read)
    TextView tvRead;
    @BindView(R.id.btn_collection)
    TextView tvCollection;
    @BindView(R.id.tv_newest_chapter)
    TextView tvNewestChapter;
    @BindView(R.id.gv_chapter)
    GridView gvChapter;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.sl_content)
    NestedScrollView slContent;
    @BindView(R.id.rl_content)
    RelativeLayout rlContent;
    @BindView(R.id.tv_comic_source)
    TextView tvSource;
    private ComicInfo comicInfo;
    private ChapterAdapter adapter;
    private List<ComicInfo.ChapterListBean> chapterList;
    private SharedPreferences sp;
    private String historyName = "";
    private String historyUrl = "";
    private int historyPos = -Integer.MAX_VALUE;
    private boolean isRead;
    private boolean isCollected;
    private List<Integer> downloadPos;
    private List<DownloadInfo> downloadInfoList;
    private boolean isCover;
    private Intent intent;
    private boolean isLoaded;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        intent = getIntent();
        initToolbar(toolbar);
        toolbar.inflateMenu(R.menu.menu_detail);
        toolbar.setTitle(intent.getStringExtra("name"));
        String cover = intent.getStringExtra("cover");
        if (!TextUtils.isEmpty(cover)) {
            HeaderGlide.loadImage(this, cover, ivCover, intent.getStringExtra("source"));
            HeaderGlide.loadBitmap(this, cover, ivBlur, intent.getStringExtra("source"));
            isCover = true;
            getWindow().getSharedElementEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if (!isLoaded) {
                        getData();
                        isLoaded = true;
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            if (!isLoaded) {
                getData();
                isLoaded = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (historyPos >= 0) {
            tvRead.setText("继续观看");
        } else {
            tvRead.setText("开始阅读");
        }
    }

    private void getData() {
        //先从数据库读取阅读记录,如果有阅读过的话返回historyUrl,跟list进行匹配得到position,再设置item的背景
        sp = getSharedPreferences("config", MODE_PRIVATE);
        presenter = new DetailPresenterImp(this);
        String name = intent.getStringExtra("name");
        ((DetailPresenterImp) presenter).queryHistoryByName(name);
        if (!sp.getBoolean("isLogin", false)) {
            ((DetailPresenterImp) presenter).queryCollectByName(name);
        } else {
            ((DetailPresenterImp) presenter).queryCollectOnNet(name);
        }
        //读取已下载的信息
        new DownloadPresenterImp(ComicDetailActivity.this).queryDownloadInfo(name, DownloadInfo.COMPLETE);
        ((DetailPresenterImp) presenter).getDetail(intent.getStringExtra("source"), intent.getStringExtra("url"));
        slContent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int scrollPixel = CommonUtil.getScreenHeight(getApplicationContext()) / 3;
                if (v.getScrollY() > 0 && v.getScrollY() < scrollPixel) {
                    //alpha值超过255的效果跟0一样
                    //426,640,853
                    int alpha = v.getScrollY() / (scrollPixel / 280);
                    appbar.setBackgroundColor(Color.argb(alpha >= 255 ? 255 : alpha, 255, 150, 12));
                }
            }
        });
    }

    @Override
    public void onGetData(ComicInfo info) {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_download:
                        Intent intent = new Intent(ComicDetailActivity.this, SelectActivity.class);
                        intent.putExtra("info", comicInfo);
                        startActivityForResult(intent, REQUEST_DOWNLOAD_CODE);
                        break;
                }
                return true;
            }
        });
        comicInfo = info;
        String cover = comicInfo.getCover();
        if (!isCover) {
            HeaderGlide.loadImage(this, cover, ivCover, info.getComic_source());
            HeaderGlide.loadBitmap(this, cover, ivBlur, info.getComic_source());
        }
        tvComicName.setText(this.comicInfo.getComic_name());
        tvComicAuthor.setText("作者:  " + comicInfo.getComic_author());
        tvComicStatus.setText("状态:  " + comicInfo.getStatus());
        String area = comicInfo.getComic_area();
        if (!TextUtils.isEmpty(area)) {
            tvComicType.setText("地区:  " + area);
        } else {
            tvComicType.setText("类别:  " + comicInfo.getComic_type());
        }
        String[] allArray = getResources().getStringArray(R.array.source_all);
        String[] shortArray = getResources().getStringArray(R.array.source_short);
        for (int index = 0; index < allArray.length; index++) {
            if (info.getComic_source().equals(shortArray[index])) {
                tvSource.setText("漫画来源: " + allArray[index]);
            }
        }
        tvNewestChapter.setText(" 更新于 " + comicInfo.getNewest_chapter_date().split(" ")[0]);
        tvDesc.setText("  " + comicInfo.getDesc().trim());
        tvSelect.setText("共" + comicInfo.getChapter_list().size() + "话");
        if (comicInfo.getChapter_list().size() == 0) {
            Toast.makeText(ComicDetailActivity.this, "此漫画不支持上架", Toast.LENGTH_SHORT).show();
        } else {
            if (adapter == null) {
                chapterList = comicInfo.getChapter_list();
                /*boolean isReverse = sp.getBoolean("isReverse", false);
                if (!isReverse) {
                    ivSort.setImageResource(R.drawable.ic_front);
                } else {
                    ivSort.setImageResource(R.drawable.ic_reverse);
                    Collections.reverse(chapterList);
                }*/
                //得到历史记录的位置
                historyPos = getHistoryPos(historyUrl);
                if (historyPos >= 0) {
                    tvRead.setText("继续观看");
                } else {
                    tvRead.setText("开始阅读");
                }
                adapter = new ChapterAdapter(chapterList, historyPos, this);
                if (downloadPos == null) {
                    downloadPos = new ArrayList<>();
                }
                if (downloadInfoList != null) {
                    for (DownloadInfo downloadInfo : downloadInfoList) {
                        downloadPos.add(getHistoryPos(downloadInfo.getChapter_url()));
                    }
                }
                if (downloadPos.size() > 0) {
                    adapter.setDownloadPos(downloadPos);
                }
            }
            gvChapter.setAdapter(adapter);
            gvChapter.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ComicDetailActivity.this, PageActivity.class);
                    intent.putExtra("url", comicInfo.getChapter_list().get(position).getChapter_url());
                    intent.putExtra("source", comicInfo.getComic_source());
                    startActivityForResult(intent, REQUEST_CHAPTER_CODE);
                }
            });
        }
        /*ivSort.setOnClickListener(new View.OnClickListener() {
            //正序倒序
            @Override
            public void onClick(View v) {
                boolean isReverse = sp.getBoolean("isReverse", false);
                if (chapterList != null) {
                    if (!isReverse) {
                        sp.edit().putBoolean("isReverse", true).apply();
                        ivSort.setImageResource(R.drawable.ic_reverse);
                    } else {
                        sp.edit().putBoolean("isReverse", false).apply();
                        ivSort.setImageResource(R.drawable.ic_front);
                    }
                    Collections.reverse(chapterList);
                    adapter.notifyDataSetChanged();
                }
            }
        });*/
        tvCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有登录,如果没有登录的话,从本地数据库操作,有的话从服务器操作
                if (!sp.getBoolean("isLogin", false)) {
                    //未登录
                    if (isCollected) {
                        ((DetailPresenterImp) presenter).deleteCollectByName(comicInfo.getComic_name());
                    } else {
                        ((DetailPresenterImp) presenter).collectComic(comicInfo);
                    }
                } else {
                    //已登录
                    if (isCollected) {
                        ((DetailPresenterImp) presenter).deleteCollectOnNet(comicInfo);
                    } else {
                        ((DetailPresenterImp) presenter).collectComicOnNet(comicInfo);
                    }
                }
            }
        });

        tvRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComicDetailActivity.this, PageActivity.class);
                if (historyPos >= 0) {
                    intent.putExtra("url", comicInfo.getChapter_list().get(historyPos).getChapter_url());
                } else {
                    intent.putExtra("url", comicInfo.getChapter_list().get(comicInfo.getChapter_list().size() - 1).getChapter_url());
                }
                intent.putExtra("source", comicInfo.getComic_source());
                startActivityForResult(intent, REQUEST_CHAPTER_CODE);
            }
        });

        //分享链接
        /*ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, comicInfo.getComic_url());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "分享到"));
            }
        });*/
        hideProgress();
    }

    private int getHistoryPos(String historyUrl) {
        for (int i = 0; i < chapterList.size(); i++) {
            if (chapterList.get(i).getChapter_url().equals(historyUrl)) {
                return i;
            }
        }
        return -2;
    }

    @Override
    public void onAddHistory(long state) {
        Logger.d("添加历史记录成功");
    }

    @Override
    public void onUpdateHistory(int state) {
        Logger.d("更新历史记录成功");
    }

    @Override
    public void onQueryHistory(String historyName, String historyUrl) {
        if (!TextUtils.isEmpty(historyName) && !TextUtils.isEmpty(historyUrl)) {
            isRead = true;      //判断是否看过
            this.historyName = historyName;
            this.historyUrl = historyUrl;
        }
        //没有阅读记录时返回""
    }

    @Override
    public void onQueryCollectByName(boolean isCollected) {
        this.isCollected = isCollected;
        changeCollection(isCollected);
    }

    @Override
    public void onAddCollect(long state) {
        isCollected = true;
        changeCollection(isCollected);
        Toast.makeText(ComicDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteCollectByName(int state) {
        isCollected = false;
        changeCollection(isCollected);
        Toast.makeText(ComicDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onQueryCollectOnNet(boolean isCollected) {
        this.isCollected = isCollected;
        changeCollection(isCollected);
    }

    @Override
    public void onAddCollectOnNet(UserInfo info) {
        isCollected = true;
        User.getInstance().saveUser(info);
        changeCollection(isCollected);
        Toast.makeText(ComicDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteCollectOnNet(UserInfo info) {
        isCollected = false;
        User.getInstance().saveUser(info);
        changeCollection(isCollected);
        Toast.makeText(ComicDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
    }

    private void changeCollection(boolean isCollected) {
        if (isCollected) {
            tvCollection.setText("已收藏");
            tvCollection.setTextColor(Color.GRAY);
            tvCollection.setBackgroundResource(R.drawable.gray_border);
        } else {
            tvCollection.setText("收藏");
            tvCollection.setTextColor(Color.WHITE);
            tvCollection.setBackgroundResource(R.drawable.btn_primary_color_select);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //将阅读记录保存到数据库
        if (resultCode == PageActivity.PAGE_RESULT_CODE) {
            historyName = data.getStringExtra("name");
            historyUrl = data.getStringExtra("url");
            if (isRead) {
                ((DetailPresenterImp) presenter).updateHistory(comicInfo, historyName, historyUrl);
                Logger.d("更新阅读记录");
            } else {
                ((DetailPresenterImp) presenter).addHistory(comicInfo, historyName, historyUrl);
                isRead = true;
                Logger.d("添加阅读记录");
            }
            historyPos = getHistoryPos(historyUrl);
            adapter.setHistoryPos(historyPos);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFail() {
//        Toast.makeText(ComicDetailActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
//        rlContent.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
        if (rlContent.getVisibility() == View.INVISIBLE) {
            rlContent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> infoList) {
        if (infoList.size() == 0) {
            return;
        }
        downloadInfoList = infoList;
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
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.closeDB();
        }
    }
}
