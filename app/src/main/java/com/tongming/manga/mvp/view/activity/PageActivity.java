package com.tongming.manga.mvp.view.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.ScrollSpeedLinearLayoutManger;
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.cusview.ZoomRecyclerView;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.presenter.DownloadPresenterImp;
import com.tongming.manga.mvp.presenter.PagePresenterImp;
import com.tongming.manga.mvp.view.adapter.PageAdapter;
import com.tongming.manga.server.DownloadInfo;
import com.tongming.manga.util.CommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PageActivity extends BaseActivity implements IPageView, IDownloadView {
    public static final int PAGE_REQUEST_CODE = 1;
    public static final int PAGE_RESULT_CODE = 10;
    private static final int NET_PAGE = 0x3388;
    private static final int LOCAL_PAGE = 0x3399;
    @BindView(R.id.rv_page)
    ZoomRecyclerView rvPage;
    //    RecyclerView rvPage;
    @BindView(R.id.iv_load)
    ImageView ivLoad;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_page_chapter_name)
    TextView tvChapterName;
    @BindView(R.id.tv_total_page)
    TextView tvTotalPage;
    @BindView(R.id.tv_current_page)
    TextView tvCurrent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_network_status)
    TextView tvNetworkStatus;
    @BindView(R.id.rl_top_bar)
    RelativeLayout rlTopBar;
    @BindView(R.id.rl_top_left)
    RelativeLayout rlTopLeft;
    @BindView(R.id.sb_page)
    AppCompatSeekBar sbPage;
    @BindView(R.id.iv_bright)
    ImageView ivBright;
    @BindView(R.id.iv_screen)
    ImageView ivScreen;
    @BindView(R.id.rl_bright)
    RelativeLayout rlBright;
    @BindView(R.id.rl_screen)
    RelativeLayout rlScreen;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.tv_screen)
    TextView tvScreen;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout rlBottomBar;
    @BindView(R.id.fl_root)
    FrameLayout flRoot;
    private boolean isControllerShowing = true;
    private Animation animation;
    private CheckBox cbCheck;
    private ImageView ivAuto;
    private SharedPreferences sp;
    private SeekBar sbBright;
    private List<String> imgList;
    private PageAdapter adapter;
    private ScrollSpeedLinearLayoutManger manager;
    private boolean isLoadNext;
    private boolean isLoadPre;
    private List<String> nameList;
    private List<Integer> numList;
    private List<String> urlList;
    private String preUrl;
    private String nextUrl;
    private String resultName;
    private String resultUrl;

    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
        }

        void update() {
            initNetTime();
        }
    };
    private int currentChapter;
    private float firstX = 0.0f;
    private float firstY = 0.0f;
    private boolean isVertical;
    private int currentPage;
    private boolean isActionDown;
    private long downTime;
    private boolean isLoadNone;
    private String source;
    private DownloadPresenterImp downloadPresenterImp;
    private ObjectAnimator animator;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_page;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            //沉浸
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @Override
    protected void initView() {
        Glide.get(this).clearMemory();
        CommonUtil.requireScreenOn(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        isVertical = sp.getBoolean("isVertical", true);
        Intent intent = getIntent();
        source = intent.getStringExtra("source");
        setOrientation();     //进来时初始化横竖屏
        setWindowBright();
        initNetTime();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        };
        timer.schedule(timerTask, 1000 * 60, 1000 * 60);
        queryDownloadInfo(intent.getStringExtra("url"));
        initRecycle();
        sbPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int position = calculateSB(progress);
                calculatePos(position);
                rvPage.scrollToPosition(position);
            }
        });
        if (presenter == null) {
            presenter = new PagePresenterImp(this);
        }
    }

    private void queryDownloadInfo(String url) {
        if (downloadPresenterImp == null) {
            downloadPresenterImp = new DownloadPresenterImp(this);
        }
        downloadPresenterImp.queryDownloadInfoByUrl(url);
    }

    private void initRecycle() {
        manager = new ScrollSpeedLinearLayoutManger(this);
        if (!isVertical) {
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            manager.setSpeed(0.2f);
        }
        rvPage.setLayoutManager(manager);
        rvPage.addItemDecoration(new SpaceItemDecoration(30));

        rvPage.addOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlide;
            boolean isLeft;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                isLeft = dx < 0;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:    //手指拖动
                        if (isControllerShowing) {
                            hideController();
                        }
                        isSlide = true;
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int position = manager.findLastVisibleItemPosition();
                        //静止状态
                        if ((sp.getBoolean("isPortrait", true) && position == manager.getItemCount() - 1)
                                || (!sp.getBoolean("isPortrait", true)) && position == manager.getItemCount() - 1) {
                            //滑动到最后一页时加载下一章
                            loadNext();
                        }
                        if (!isVertical && isSlide && sp.getBoolean("isPortrait", true)) {
                            if (isLeft) {
                                rvPage.smoothScrollToPosition(manager.findFirstVisibleItemPosition());
                            } else {
                                rvPage.smoothScrollToPosition(manager.findLastVisibleItemPosition());
                            }
                            isSlide = false;
                        }
                        calculatePos(position);
                        currentPage = position;
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }
            }
        });
    }

    //动态计算章节名和页数
    private void calculatePos(int position) {
        outer:
        for (int i = 0; i < numList.size(); i++) {
            int sum = 0;
            for (int j = 0; j < i + 1; j++) {
                sum += numList.get(j);
                if ((position + 1 - sum) <= 0) {
                    tvChapterName.setText(nameList.get(j));
                    tvTotalPage.setText(" / " + numList.get(j));
                    sbPage.setMax(numList.get(j) - 1);
                    resultName = nameList.get(j);
                    resultUrl = urlList.get(j);
                    currentChapter = j;
                    int currentPos = position + 1;
                    if (j > 0) {
                        for (int n = 0; n < j; n++) {
                            currentPos -= numList.get(n);
                        }
                    }
                    tvCurrent.setText(currentPos + "");
                    sbPage.setProgress(currentPos);
                    break outer;
                }
            }
        }
    }

    private int calculateSB(int position) {
        int sum = 0;
        if (currentChapter != 0) {
            for (int i = 0; i <= currentChapter - 1; i++) {
                sum += numList.get(i);
            }
        }
        sum += position;
        return sum;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int width = CommonUtil.getScreenWidth(getApplicationContext());
        int height = CommonUtil.getScreenHeight(getApplicationContext());
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = ev.getRawX();
                firstY = ev.getRawY();
                isActionDown = true;
                downTime = System.currentTimeMillis();
                if (imgList == null) {
                    Toast.makeText(this, "正在加载", Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(firstX - width / 2) < width / 3
                        && isActionDown
                        && ((System.currentTimeMillis() - downTime) < 300)
                        && Math.abs(firstY - ev.getRawY()) < 5) {
                    if (isControllerShowing) {
                        hideController();
                    } else {
                        showController();
                    }
                    //不返回true,不拦截此处的事件 :( 绕了好久,为什么要这么傻去拦截...
                }
                //加载上一话
                if ((manager.findFirstVisibleItemPosition() == 0)
                        || (!sp.getBoolean("isPortrait", true) && manager.findFirstVisibleItemPosition() == 0)) {
                    if (sp.getBoolean("isPortrait", true)) {
                        if ((isVertical && (ev.getRawY() - firstY) > 20f)
                                || !isVertical && (ev.getRawX() - firstX > 20f)) {
                            loadPre();
                        }
                    } else {
                        if (Math.abs(firstX - ev.getRawX()) > 20f) {
                            loadPre();
                        }
                    }
                }
                if (!isVertical && sp.getBoolean("isPortrait", true)) {
                    //点击屏幕左右边缘切页
                    if (Math.abs(firstY - height / 2) < 500 && Math.abs(ev.getRawX() - firstX) < 10f) {
                        if (firstX < width / 3) {
                            if (manager.findFirstVisibleItemPosition() == 0) {
                                loadPre();
                            } else {
                                rvPage.smoothScrollToPosition(manager.findFirstVisibleItemPosition() - 1);
                            }
                        } else if (firstX > width / 3 * 2) {
                            if (manager.findLastVisibleItemPosition() == manager.getItemCount() - 1) {
                                loadNext();
                            } else {
                                rvPage.smoothScrollToPosition(manager.findLastVisibleItemPosition() + 1);
                            }
                        }
                        calculatePos(manager.findFirstVisibleItemPosition());
                    }
                }
                isActionDown = false;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void showController() {
        ObjectAnimator.ofFloat(rlTopBar, "translationY", -rlTopBar.getHeight(), 0).start();
        ObjectAnimator.ofFloat(rlBottomBar, "translationY", rlBottomBar.getHeight(), 0).start();
//        ObjectAnimator.ofFloat(rlBright, "translationX", 0).start();
        isControllerShowing = true;
    }

    private void hideController() {
        ObjectAnimator.ofFloat(rlTopBar, "translationY", 0, -rlTopBar.getHeight()).start();
        ObjectAnimator.ofFloat(rlBottomBar, "translationY", 0, rlBottomBar.getHeight()).start();
//        ObjectAnimator.ofFloat(rlBright, "translationX", -rlBright.getWidth()).start();
        isControllerShowing = false;
    }

    private void initNetTime() {
        //判断网络状态
        if (CommonUtil.isWifiConnected(this)) {
            tvNetworkStatus.setText("WIFI");
        } else {
            tvNetworkStatus.setText(CommonUtil.getNetWorkClass(this) + "G");
        }
        tvTime.setText(CommonUtil.millisToStringDate(System.currentTimeMillis(), "HH:mm"));
    }

    @Override
    public void onSuccess(ComicPage page) {
        parserPage(page, NET_PAGE);
    }

    @Override
    public void onPageCompleted(List<String> imgList) {
        pageComplete(imgList);
    }

    @Override
    public void onQueryDownloadInfo(ComicPage page) {
        if (nameList == null) {
            nameList = new ArrayList<>();
        }
        if (numList == null) {
            numList = new ArrayList<>();
        }
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        if (page == null) {
            ((PagePresenterImp) presenter).getPage(source,
                    imgList == null ? getIntent().getStringExtra("url") : (isLoadNext ? nextUrl : preUrl));
            return;
        }
        int size = readPage(page.getComic_name(), page.getChapter_name()).size();
        if (size > 0) {
            List<String> imgList = readPage(page.getComic_name(), page.getChapter_name());
            page.setImg_list(imgList);
            page.setPage_count(imgList.size());
            parserPage(page, LOCAL_PAGE);
        } else {
            ((PagePresenterImp) presenter).getPage(source,
                    imgList == null ? getIntent().getStringExtra("url") : (isLoadNext ? nextUrl : preUrl));
        }
    }

    @Override
    public void onDeleteDownloadInfo(int state) {

    }

    private void parserPage(ComicPage page, int flag) {
        String chapterName = page.getChapter_name();
        Matcher matcher = Pattern.compile("第?\\d+[话卷集回]").matcher(chapterName);
        if (matcher.find()) {
            chapterName = matcher.group(0);
        }
        if (imgList == null) {
            if (page.isPrepare()) {
                preUrl = page.getPre_chapter_url();
            }
            if (page.isNext()) {
                nextUrl = page.getNext_chapter_url();
            }
            numList.add(page.getPage_count());
            nameList.add(chapterName);
            resultName = chapterName;
            urlList.add(page.getCurrent_chapter_url());
            resultUrl = page.getCurrent_chapter_url();
            tvChapterName.setText(chapterName);
            tvTotalPage.setText(" / " + page.getImg_list().size());
            tvCurrent.setText("1");
            sbPage.setMax(page.getImg_list().size() - 1);
            sbPage.setProgress(1);
        } else if (isLoadNext) {
            nameList.add(chapterName);
            numList.add(page.getPage_count());
            urlList.add(page.getCurrent_chapter_url());
            nextUrl = page.isNext() ? page.getNext_chapter_url() : null;
        } else {
            nameList.add(0, chapterName);
            numList.add(0, page.getPage_count());
            urlList.add(0, page.getCurrent_chapter_url());
            preUrl = page.isPrepare() ? page.getPre_chapter_url() : null;
        }
        if (flag == NET_PAGE) {
            ((PagePresenterImp) presenter).cacheImg(this, page.getImg_list(), false);
        } else if (flag == LOCAL_PAGE) {
            pageComplete(page.getImg_list());
        }
    }

    private void pageComplete(List<String> imgList) {
        if (this.imgList == null) {
            this.imgList = imgList;
            adapter = new PageAdapter(this.imgList, this, source);
            rvPage.setAdapter(adapter);
        } else {
            int itemCount = imgList.size();
            int startPos = 0;
            if (isLoadNext) {
                isLoadNext = false;
                startPos = this.imgList.size();
                this.imgList.addAll(imgList);
                adapter.notifyItemRangeInserted(startPos, itemCount);
            } else if (isLoadPre) {
                isLoadPre = false;
                this.imgList.addAll(0, imgList);
                adapter.notifyItemRangeInserted(startPos, itemCount);
                manager.scrollToPosition(imgList.size());
            }
        }
    }

    private void loadPre() {
        if (!isLoadPre && !TextUtils.isEmpty(preUrl)) {
            queryDownloadInfo(preUrl);
            isLoadPre = true;
        } else if (TextUtils.isEmpty(preUrl)) {
            Toast.makeText(PageActivity.this, "这是第一话哦", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNext() {
        if (!TextUtils.isEmpty(nextUrl) && !isLoadNext && imgList != null) {
            queryDownloadInfo(nextUrl);
            isLoadNext = true;
        } else if (TextUtils.isEmpty(nextUrl)) {
            //只显示一次
            if (!isLoadNone) {
                Toast.makeText(PageActivity.this, "下面没有咯", Toast.LENGTH_SHORT).show();
                isLoadNone = true;
            }
        }
    }

    /**
     * 读取本地图片
     *
     * @param chapterName 章节名
     * @param comicName   漫画名
     */
    private List<String> readPage(String comicName, String chapterName) {
        List<String> imgList = new ArrayList<>();
        String chapterPath = BaseApplication.getExternalPath() + "/download/" + comicName + "/" + chapterName;
        File file = new File(chapterPath);
        if (file.exists() && file.isDirectory()) {
            for (File f : file.listFiles()) {
                imgList.add(f.getAbsolutePath());
            }
        }
        return imgList;
    }

    private void setResult(String resultName) {
        Intent intent = new Intent();
        setResult(PAGE_RESULT_CODE, intent.putExtra("name", resultName).putExtra("url", resultUrl));
    }

    @Override
    public void onQueryDownloadInfo(List<DownloadInfo> infoList) {

    }

    @Override
    public void onFail(Throwable throwable) {
        Toast.makeText(PageActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
        if (imgList == null) {
            rvPage.setVisibility(View.GONE);
        }
        Logger.e(throwable.getMessage());
    }

    @Override
    public void onBackPressed() {
        setResult(resultName);
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }

    @Override
    public void showProgress() {
        ivLoad.setVisibility(View.VISIBLE);
        animator = ObjectAnimator.ofFloat(ivLoad, "translationX", 20);
        animator.setRepeatCount(Integer.MAX_VALUE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    public void hideProgress() {
        if (ivLoad.getVisibility() == View.VISIBLE) {
            ivLoad.setVisibility(View.GONE);
            animator.cancel();
        }
    }

    private void setOrientation() {
        int width = CommonUtil.getScreenWidth(this);
        int height = CommonUtil.getScreenHeight(this);
        if (!sp.getBoolean("isPortrait", false)) {
            //切换为横屏
            if (height > width) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (height < width) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    private void setWindowBright() {
        boolean isSystemBright = sp.getBoolean("isSystemBright", true);
        if (!isSystemBright) {
            //从sp中获取保存的亮度
            int windowBright = sp.getInt("windowBright", CommonUtil.getSystemScreenBrightness(this));
            CommonUtil.setScreenBrightness(this, windowBright);
        } else {
            CommonUtil.setScreenBrightness(this, CommonUtil.getSystemScreenBrightness(this));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int width = CommonUtil.getScreenWidth(this);
        int height = CommonUtil.getScreenHeight(this);
        if (height < width) {
            if (!sp.getBoolean("isVertical", true)) {
                manager.setOrientation(LinearLayoutManager.VERTICAL);
            }
            sp.edit().putBoolean("isPortrait", false).apply();
        } else {
            if (!sp.getBoolean("isVertical", true)) {
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }
            sp.edit().putBoolean("isPortrait", true).apply();
        }
        if (imgList != null) {
            adapter = new PageAdapter(imgList, this, source);
            rvPage.setAdapter(adapter);
            manager.scrollToPosition(currentPage);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAGE_REQUEST_CODE) {
            if (sp.getBoolean("isVertical", true) != isVertical) {
                //配置发生改变
                isVertical = sp.getBoolean("isVertical", true);
                if (!isVertical) {
                    manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    manager.setSpeed(0.2f);
                    if (imgList.size() != 0) {
                        adapter = new PageAdapter(imgList, this, source);
                        rvPage.setAdapter(adapter);
                    }
                } else {
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                }
            }
            setOrientation();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        timerTask.cancel();
        timerTask = null;
        CommonUtil.releaseScreenOn(this);   //取消屏幕常亮
        super.onDestroy();
    }

    private void showBrightDialog() {
        View dialog = View.inflate(this, R.layout.dialog_bright, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialog).show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(126, 49, 39, 26)));
        sbBright = (SeekBar) dialog.findViewById(R.id.sb_bright);
        cbCheck = (CheckBox) dialog.findViewById(R.id.cb_auto);
        ivAuto = (ImageView) dialog.findViewById(R.id.iv_bright);
        boolean isSystemBright = sp.getBoolean("isSystemBright", true);
        if (isSystemBright) {
            sbBright.setProgress(CommonUtil.getSystemScreenBrightness(getApplicationContext()) + 1);
            cbCheck.setChecked(true);
        } else {
            //从sp中获取保存的亮度
            int windowBright = sp.getInt("windowBright", 255 / 2);
            sbBright.setProgress(windowBright);
            cbCheck.setChecked(false);
        }
        sbBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (cbCheck.isChecked()) {
                    cbCheck.setChecked(false);
                    sp.edit().putBoolean("isSystemBright", false).apply();
                }
                //设置系统亮度
                CommonUtil.setScreenBrightness(PageActivity.this, progress);
                if (progress > seekBar.getMax() / 2) {
                    ivAuto.setImageResource(R.drawable.ic_brightness_high_amber_400_18dp);
                } else if (progress < seekBar.getMax() / 2) {
                    ivAuto.setImageResource(R.drawable.ic_brightness_low_amber_400_18dp);
                } else {
                    ivAuto.setImageResource(R.drawable.ic_brightness_medium_amber_400_18dp);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //保存亮度
                sp.edit().putInt("windowBright", seekBar.getProgress()).apply();
            }
        });
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor edit = sp.edit();
                if (isChecked) {
                    edit.putBoolean("isSystemBright", true);
                    CommonUtil.setScreenBrightness(PageActivity.this, CommonUtil.getSystemScreenBrightness(getApplicationContext()));
                } else {
                    edit.putBoolean("isSystemBright", false);
                    CommonUtil.setScreenBrightness(PageActivity.this, sp.getInt("windowBright", 255 / 2));
                }
                edit.apply();
            }
        });
    }

    @OnClick({R.id.rl_top_left, R.id.rl_bright, R.id.rl_screen, R.id.rl_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_top_left:
                onBackPressed();
                break;
            case R.id.rl_bright:
                showBrightDialog();
                break;
            case R.id.rl_screen:
                boolean isPortrait = sp.getBoolean("isPortrait", true);
                if (isPortrait) {
                    sp.edit().putBoolean("isPortrait", false).apply();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    sp.edit().putBoolean("isPortrait", true).apply();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.rl_setting:
                Intent intent = new Intent(PageActivity.this, WatchSettingActivity.class);
                startActivityForResult(intent, PAGE_REQUEST_CODE);
                break;
        }
    }
}
