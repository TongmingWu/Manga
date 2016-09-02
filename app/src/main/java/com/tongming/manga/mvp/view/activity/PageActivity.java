package com.tongming.manga.mvp.view.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.tongming.manga.cusview.SpaceItemDecoration;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.presenter.PagePresenterImp;
import com.tongming.manga.mvp.view.adapter.PageAdapter;
import com.tongming.manga.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PageActivity extends BaseActivity implements IPageView {
    public static final int PAGE_REQUEST_CODE = 1;
    public static final int PAGE_RESULT_CODE = 10;
    @BindView(R.id.rv_page)
    RecyclerView rvPage;
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
    @BindView(R.id.tv_screen)
    TextView tvScreen;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rl_bottom_bar)
    RelativeLayout rlBottomBar;
    @BindView(R.id.fl_root)
    FrameLayout flRoot;
    /*@BindView(R.id.iv_bright)
    ImageView ivBright;
    @BindView(R.id.sb_bright)
    VerticleSeekBar sbBright;
    @BindView(R.id.rl_bright)
    RelativeLayout rlBright;*/
    private boolean isControllerShowing = true;
    private Animation animation;
    private CheckBox cbCheck;
    private ImageView ivAuto;
    private SharedPreferences sp;
    private SeekBar sbBright;
    private List<String> imgList;
    private PageAdapter adapter;
    private LinearLayoutManager manager;
    private boolean isLoadNext;
    private boolean isLoadPre;
    private boolean isFirstLoad;
    private float lastY = 0;
    private List<String> nameList;
    private List<Integer> numList;
    private List<String> urlList;
    private String lastUrl;
    private String nextUrl;
    private String resultName;
    private String resultUrl;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
            handler.postDelayed(runnable, 1000 * 60);
            Logger.d("刷新完成");
        }

        void update() {
            initNetTime();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
        handler.postDelayed(runnable, 1000 * 60);      //时间定时器
        sp = getSharedPreferences("config", MODE_PRIVATE);
//        setOrientation();     //进来时初始化横竖屏
        setWindowBright();
        initNetTime();
        if (!isFirstLoad) {
            isFirstLoad = true;
            presenter = new PagePresenterImp(this);
            ((PagePresenterImp) presenter).getPage(getIntent().getStringExtra("url"));
        }
       /* //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //去掉虚拟按键全屏显示
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);*/
        initRecycle();
        sbPage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
//                calculatePos(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停止拖动时跳转图片
                rvPage.scrollToPosition(progress);
            }
        });
    }

    private void initRecycle() {
        manager = new LinearLayoutManager(this);
        rvPage.setLayoutManager(manager);
        rvPage.addItemDecoration(new SpaceItemDecoration(30));
        rvPage.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        //正在滑动
//                        tvCurrent.setText(position + 1 + "");
                        if (isControllerShowing) {
                            hideController();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        int position = manager.findFirstVisibleItemPosition();
                        //静止状态
                        if (manager.findFirstCompletelyVisibleItemPosition() == manager.getItemCount() - 1) {
                            //滑动到最后一页时加载下一章
                            if (!TextUtils.isEmpty(nextUrl) && !isLoadNext && !isFirstLoad) {
                                Logger.d("开始加载下一章");
                                ((PagePresenterImp) presenter).getPage(nextUrl);
                                isLoadNext = true;
                            } else if (TextUtils.isEmpty(nextUrl)) {
                                Toast.makeText(PageActivity.this, "下面没有咯", Toast.LENGTH_SHORT).show();
                            }
                        }
                        sbPage.setProgress(position);
                        calculatePos(manager.findFirstVisibleItemPosition());
                        break;
                }
            }
        });
        rvPage.setOnTouchListener(new View.OnTouchListener() {
            long lastTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (System.currentTimeMillis() - lastTime < 1000) {

                        lastTime = 0;
                    } else {
                        lastTime = System.currentTimeMillis();
                    }
                }
                return false;
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
                if ((position + 1 - sum) < 0) {
                    tvChapterName.setText(nameList.get(j));
                    tvTotalPage.setText(" / " + numList.get(j) + "");
                    resultName = nameList.get(j);
                    resultUrl = urlList.get(j);
                    int currentPos = position + 1;
                    if (j > 0) {
                        for (int n = 0; n < j; n++) {
                            currentPos -= numList.get(n);
                        }
                    }
                    tvCurrent.setText(currentPos + 1 + "");
                    break outer;
                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float rowX = ev.getRawX();
                float rowY = ev.getRawY();
                int width = CommonUtil.getDeviceWidth(getApplicationContext());
                int height = CommonUtil.getDeviceHeight(getApplicationContext());
                if (Math.abs((int) rowX - width / 2) < 400 && Math.abs((int) rowY - height / 2) < 400) {
                    if (isControllerShowing) {
                        hideController();
                    } else {
                        showController();
                    }
                    //不返回true,不拦截此处的事件 :( 绕了好久,为什么要这么傻去拦截...
                }
                if (isFirstLoad) {
                    Logger.d("正在加载图片,不允许滑动");
                    return true;
                }
                //在第一页的时候向下滑动,请求上一话
                if (manager.findFirstCompletelyVisibleItemPosition() == 0) {
                    lastY = rowY;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (manager.findFirstCompletelyVisibleItemPosition() == 0 && (ev.getRawY() - lastY) > 20f) {
                    loadLast();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void loadLast() {
        if (!isLoadPre && !TextUtils.isEmpty(lastUrl)) {
            ((PagePresenterImp) presenter).getPage(lastUrl);
            Logger.d("开始加载上一话");
            isLoadPre = true;
        } else if (TextUtils.isEmpty(lastUrl)) {
            Toast.makeText(PageActivity.this, "这是第一话哦", Toast.LENGTH_SHORT).show();
        }
    }

    private void showController() {
        ObjectAnimator.ofFloat(rlTopBar, "translationY", 0).start();
        ObjectAnimator.ofFloat(rlBottomBar, "translationY", 0).start();
//        ObjectAnimator.ofFloat(rlBright, "translationX", 0).start();
        isControllerShowing = true;
    }

    private void hideController() {
        ObjectAnimator.ofFloat(rlTopBar, "translationY", -rlTopBar.getHeight()).start();
        ObjectAnimator.ofFloat(rlBottomBar, "translationY", rlBottomBar.getHeight()).start();
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
        if (nameList == null) {
            nameList = new ArrayList<>();
        }
        if (numList == null) {
            numList = new ArrayList<>();
        }
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        if (isFirstLoad) {
            if (page.isPrepare()) {
                lastUrl = page.getLast_chapter_url();
            }
            if (page.isNext()) {
                nextUrl = page.getNext_chapter_url();
            }
            numList.add(page.getPage_count());
            nameList.add(page.getChapter_name());
            resultName = page.getChapter_name();
            urlList.add(page.getCurrent_chapter_url());
            Logger.d("当前的urlList = :" + urlList.toString());
            resultUrl = page.getCurrent_chapter_url();
        } else if (isLoadNext) {
            nameList.add(page.getChapter_name());
            numList.add(page.getPage_count());
            urlList.add(page.getCurrent_chapter_url());
            Logger.d("当前的urlList = :" + urlList.toString());
            if (page.isNext()) {
                nextUrl = page.getNext_chapter_url();
            } else {
                nextUrl = null;
            }
        } else {
            nameList.add(0, page.getChapter_name());
            numList.add(0, page.getPage_count());
            urlList.add(0, page.getCurrent_chapter_url());
            Logger.d("当前的urlList = :" + urlList.toString());
            if (page.isPrepare()) {
                lastUrl = page.getLast_chapter_url();
            } else {
                lastUrl = null;
            }
        }
        if (page.getImg_list().size() > 0) {
            //先将图片缓存到磁盘
            ((PagePresenterImp) presenter).cacheImg(this, page.getImg_list());
        } else {
            Toast.makeText(PageActivity.this, "没有图片", Toast.LENGTH_SHORT).show();
        }
        tvChapterName.setText(page.getChapter_name());
        tvTotalPage.setText(" / " + page.getImg_list().size());
        tvCurrent.setText("1");
    }

    @Override
    public void onPageCompleted(List<String> imgList) {
        if (this.imgList == null) {
            this.imgList = imgList;
            adapter = new PageAdapter(this.imgList, this);
            rvPage.setAdapter(adapter);
            isFirstLoad = false;
            Logger.d("第一次加载成功");
        } else {
            if (isLoadNext) {
                Logger.d("加载下一话成功");
                isLoadNext = false;
                this.imgList.addAll(imgList);
            } else if (isLoadPre) {
                Logger.d("加载上一话成功");
                //TODO 加载上一话的时候不要跳转到第一页去
                isLoadPre = false;
                /*List<String> reverseList = imgList;
                Collections.reverse(reverseList);*/
                this.imgList.addAll(0, imgList);
            }
            Logger.d(numList.toString());
            adapter.notifyDataSetChanged();
            sbPage.setMax(manager.getItemCount());
            sbPage.setProgress(manager.findFirstCompletelyVisibleItemPosition());
        }
    }

    private void setResult(String resultName) {
        Intent intent = new Intent();
        setResult(PAGE_RESULT_CODE, intent.putExtra("name", resultName).putExtra("url", resultUrl));
    }

    @Override
    public void onFail() {
        Toast.makeText(PageActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(resultName);
        Logger.d("在back中setResult");
        handler.removeCallbacks(runnable);
        super.onBackPressed();
    }

    @Override
    public void showProgress() {
        ivLoad.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(this, R.anim.anim_load_page);
        ivLoad.startAnimation(animation);
    }

    @Override
    public void hideProgress() {
        if (ivLoad.getVisibility() == View.VISIBLE) {
            ivLoad.setVisibility(View.GONE);
            animation.cancel();
        }
    }

    private void setOrientation() {
        int width = CommonUtil.getDeviceWidth(getApplicationContext());
        int height = CommonUtil.getDeviceHeight(getApplicationContext());
        if (sp.getBoolean("isPortrait", true)) {
            if (width > height) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        } else {
            if (width < height) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    private void setWindowBright() {
        boolean isSystemBright = sp.getBoolean("isSystemBright", true);
        if (!isSystemBright) {
            //从sp中获取保存的亮度
            int windowBright = sp.getInt("windowBright", 255 / 2);
            CommonUtil.setScreenBrightness(this, windowBright);
        } else {
            CommonUtil.setScreenBrightness(this, CommonUtil.getSystemScreenBrightness(this));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.d("configChanged");
        //TODO 做图片的放大处理

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //设置返回处理
        Logger.d("settingResult");
    }

    @Override
    protected void onDestroy() {
        Glide.get(BaseApplication.getContext()).clearMemory();
        handler.removeCallbacks(runnable);
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
            if (!cbCheck.isChecked()) {
                cbCheck.setChecked(true);
            }
        } else {
            //从sp中获取保存的亮度
            int windowBright = sp.getInt("windowBright", 255 / 2);
            sbBright.setProgress(windowBright);
            if (cbCheck.isChecked()) {
                cbCheck.setChecked(false);
            }
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

            }
        });
    }

    @OnClick({R.id.rl_top_left, R.id.iv_bright, R.id.iv_screen, R.id.iv_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_top_left:
                onBackPressed();
                break;
            case R.id.iv_bright:
                showBrightDialog();
                break;
            case R.id.iv_screen:
                boolean isPortrait = sp.getBoolean("isPortrait", true);
                if (isPortrait) {
                    sp.edit().putBoolean("isPortrait", false).apply();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    sp.edit().putBoolean("isPortrait", true).apply();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.iv_setting:
                Intent intent = new Intent(PageActivity.this, WatchSettingActivity.class);
                startActivityForResult(intent, PAGE_REQUEST_CODE);
                break;
        }
    }
}
