package com.tongming.manga.mvp.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.presenter.DetailPresenterImp;
import com.tongming.manga.mvp.view.adapter.ChapterAdapter;
import com.tongming.manga.util.FastBlur;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Tongming on 2016/8/10.
 */
public class ComicDetailActivity extends BaseActivity implements IDetailView {
    public static final int REQUEST_CHAPTER_CODE = 0x15;
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
    @BindView(R.id.iv_fav)
    ImageView ivFav;
    @BindView(R.id.iv_download)
    ImageView ivDownload;
    /*@BindView(R.id.iv_sort)
    ImageView ivSort;*/
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
    @BindView(R.id.rl_cover)
    RelativeLayout rlCover;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    private ComicInfo info;
    private ChapterAdapter adapter;
    private List<ComicInfo.ChapterListBean> chapterList;
    private SharedPreferences sp;
    private String historyName = "";
    private String historyUrl = "";
    private int historyPos;
    private boolean isRead;
    private boolean isCollected;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initView() {
        presenter = new DetailPresenterImp(this);
        //先从数据库读取阅读记录,如果有阅读过的话返回historyUrl,跟list进行匹配得到position,在设置item的背景
        String name = getIntent().getStringExtra("name");
        ((DetailPresenterImp) presenter).queryHistoryByName(name);
        ((DetailPresenterImp) presenter).queryCollectByName(name);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toolbar.getLayoutParams();
        params.setMargins(0, 80, 0, 0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((DetailPresenterImp) presenter).getDetail(getIntent().getStringExtra("url"));
        slContent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (v.getScrollY() > 0 && v.getScrollY() < 765) {
                    appbar.setBackgroundColor(Color.argb(v.getScrollY() / 3, 255, 150, 12));
                }
            }
        });
    }

    @Override
    public void onGetData(final ComicInfo info) {
        this.info = info;
        Glide.with(this)
                .load(info.getCover())
                .priority(Priority.LOW)
                .into(ivCover);
        Glide.with(this)
                .load(info.getCover())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ivBlur.setImageBitmap(FastBlur.doBlur(resource, 5, false));
                    }
                });
        tvComicName.setText(info.getComic_name());
        tvComicAuthor.setText("作者:  " + info.getComic_author());
        tvComicStatus.setText("状态:  " + info.getStatus());
        tvComicType.setText("地区:  " + info.getComic_area());
        tvNewestChapter.setText(" 更新于 " + info.getNewest_chapter_date().split(" ")[0]);
        tvDesc.setText("  " + info.getDesc());
        tvSelect.setText("共" + info.getChapter_list().size() + "话");
        if (info.getChapter_list().size() == 0) {
            Toast.makeText(ComicDetailActivity.this, "此漫画不支持上架", Toast.LENGTH_SHORT).show();
        } else {
            if (adapter == null) {
                chapterList = info.getChapter_list();
                /*boolean isReverse = sp.getBoolean("isReverse", false);
                if (!isReverse) {
                    ivSort.setImageResource(R.drawable.ic_front);
                } else {
                    ivSort.setImageResource(R.drawable.ic_reverse);
                    Collections.reverse(chapterList);
                }*/
                //得到历史记录的位置
                historyPos = getHistoryPos(historyUrl);
                Logger.d(historyPos);
                adapter = new ChapterAdapter(chapterList, historyPos, this);
            }
            gvChapter.setAdapter(adapter);
            gvChapter.setSelector(new ColorDrawable(Color.TRANSPARENT));
            gvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ComicDetailActivity.this, PageActivity.class);
                    intent.putExtra("url", info.getChapter_list().get(position).getChapter_url());
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
        //收藏
        ivFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCollected) {
                    ((DetailPresenterImp) presenter).deleteCollectByName(info.getComic_name());
                } else {
                    ((DetailPresenterImp) presenter).collectComic(info);
                }
            }
        });

        //下载漫画


        //分享链接
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, info.getComic_name() + "http://m.tuku.cc" + info.getComic_url());
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent, "分享到"));
            }
        });
    }

    private int getHistoryPos(String historyUrl) {
        for (int i = 0; i < chapterList.size(); i++) {
            if (chapterList.get(i).getChapter_url().equals(historyUrl)) {
                Logger.d("计算的url=" + chapterList.get(i).getChapter_url());
                return i;
            }
        }
        return -1;
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
        if (!TextUtils.isEmpty(historyName)) {
            Logger.d("读取阅读记录成功:" + historyUrl);
            isRead = true;      //判断是否看过
            this.historyName = historyName;
            this.historyUrl = historyUrl;
        }
        //没有阅读记录时返回""
    }

    @Override
    public void onQueryCollectByName(boolean isCollected) {
        this.isCollected = isCollected;
        if (isCollected) {
            ivFav.setImageResource(R.drawable.ic_collected);
        }
    }

    @Override
    public void onAddCollect(long state) {
        if (state > 0) {
            ivFav.setImageResource(R.drawable.ic_collected);
            isCollected = true;
            Toast.makeText(ComicDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteCollectByName(int state) {
        if (state > 0) {
            isCollected = false;
            ivFav.setImageResource(R.drawable.select_collect);
            Toast.makeText(ComicDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //将阅读记录保存到数据库
        if (resultCode == PageActivity.PAGE_RESULT_CODE) {
            historyName = data.getStringExtra("name");
            historyUrl = data.getStringExtra("url");
            if (isRead) {
                ((DetailPresenterImp) presenter).updateHistory(info, historyName, historyUrl);
                Logger.d("更新阅读记录");
            } else {
                ((DetailPresenterImp) presenter).addHistory(info, historyName, historyUrl);
                Logger.d("添加阅读记录");
            }
            Logger.d("返回的url=" + historyUrl);
            adapter = new ChapterAdapter(chapterList, getHistoryPos(historyUrl), this);
            gvChapter.setAdapter(adapter);
        }
    }

    @Override
    public void onFail() {
        Toast.makeText(ComicDetailActivity.this, "服务器出错", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        rlContent.setVisibility(View.INVISIBLE);
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
}
