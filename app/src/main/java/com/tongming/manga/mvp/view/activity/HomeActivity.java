package com.tongming.manga.mvp.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.GlideGircleTransform;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.presenter.CachePresenterImp;
import com.tongming.manga.mvp.presenter.SystemPresenterImp;
import com.tongming.manga.mvp.view.fragment.CategoryFragment;
import com.tongming.manga.mvp.view.fragment.CollectionFragment;
import com.tongming.manga.mvp.view.fragment.HomeFragment;
import com.tongming.manga.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements ISystemView, ICacheView {

    private static final int REQUEST_PERMISSION_CODE = 1;
    public static final int LOGIN_CODE = 0x77;
    private long exitTime = 0;
    private final int[] tabPics = {
            R.drawable.bg_tab_fav,
            R.drawable.bg_tab_recom,
            R.drawable.bg_tab_search
    };
    private final int[] selectedPics = {
            R.drawable.icon_tab_favor,
            R.drawable.icon_tab_recom,
            R.drawable.icon_tab_search
    };
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp_content)
    ViewPager viewPager;
    @BindView(R.id.nav)
    NavigationView nav;
    @BindView(R.id.dl_root)
    DrawerLayout drawerLayout;
    @BindView(R.id.rl_bar)
    RelativeLayout rlBar;
    @BindView(R.id.ll_source)
    LinearLayout llSource;
    @BindView(R.id.tv_source)
    TextView tvSource;
    private SharedPreferences sp;
    private Button btnRegister;
    private ImageView navAvatar;
    private TextView tvUserName;
    private CachePresenterImp cachePresenterImp;
    private List<BaseFragment> fragments;
    private AlertDialog sourceDialog;

    @OnClick({R.id.iv_nav, R.id.iv_avatar, R.id.ll_source})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_nav:
            case R.id.iv_avatar:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.ll_source:
                changeSource();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        initToolbar(rlBar);
        fragments = new ArrayList<>();
        CollectionFragment collectionFragment = new CollectionFragment();
        HomeFragment homeFragment = new HomeFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        fragments.add(collectionFragment);
        fragments.add(homeFragment);
        fragments.add(categoryFragment);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                CommonUtil.hideSoftInput(HomeActivity.this);
                if (position != 2) {
                    CategoryFragment fragment = (CategoryFragment) fragments.get(2);
                    fragment.hideHint();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tab.setupWithViewPager(viewPager);
        for (int i = 0; i < tabPics.length; i++) {
            tab.getTabAt(i).setIcon(tabPics[i]);
        }
        tab.getTabAt(1).select();
        tab.getTabAt(1).setIcon(selectedPics[1]);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedIcon(tab);
                if (tab.getPosition() == 0) {
                    nav.setCheckedItem(R.id.menu_collected);
                } else {
                    nav.setCheckedItem(R.id.menu_index);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                unselectedIcon(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        presenter = new SystemPresenterImp(this);
        cachePresenterImp = new CachePresenterImp(this);
        initDrawerView();
        String[] shortArray = getResources().getStringArray(R.array.source_short);
        String[] allArray = getResources().getStringArray(R.array.source_all);
        String source = sp.getString("source", ApiManager.SOURCE_DMZJ);
        for (int index = 0; index < shortArray.length; index++) {
            if (source.equals(shortArray[index])) {
                tvSource.setText(allArray[index]);
                break;
            }
        }
    }

    private void initDrawerView() {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_index:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_local:
                        startActivityForResult(new Intent(HomeActivity.this, LocalActivity.class), REQUEST_PERMISSION_CODE);
                        break;
                    case R.id.menu_history:
                        startActivityForResult(new Intent(HomeActivity.this, HistoryActivity.class), REQUEST_PERMISSION_CODE);
                        break;
                    case R.id.menu_collected:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_download:
                        startActivityForResult(new Intent(HomeActivity.this, DownloadManagerActivity.class), REQUEST_PERMISSION_CODE);
                        break;
                    case R.id.menu_setting:
                        startActivityForResult(new Intent(HomeActivity.this, SettingActivity.class), REQUEST_PERMISSION_CODE);
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
        View headerView = nav.getHeaderView(0);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        btnRegister = (Button) headerView.findViewById(R.id.btn_register);
        navAvatar = (ImageView) headerView.findViewById(R.id.iv_avatar);
        tvUserName = (TextView) headerView.findViewById(R.id.tv_user_name);
        initUser();
    }


    private void initUser() {
        if (!sp.getBoolean("isLogin", false)) {
            navAvatar.setImageResource(R.drawable.default_avatar);
            ivAvatar.setImageResource(R.drawable.default_avatar);
            tvUserName.setText("未登录");
            btnRegister.setVisibility(View.VISIBLE);
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, LogonActivity.class));
                }
            });
            navAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(HomeActivity.this, LoginActivity.class), LOGIN_CODE);
                }
            });
            refreshCollection();
        } else {
            btnRegister.setVisibility(View.GONE);
            navAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(HomeActivity.this, PersonCenterActivity.class), REQUEST_PERMISSION_CODE);
                }
            });
            //从保存的User中获取用户信息
            ((SystemPresenterImp) presenter).readUser();
        }
    }

    private void refreshCollection() {
        //通知CollectionFragment页面刷新数据
        CollectionFragment fragment = (CollectionFragment) fragments.get(0);
        fragment.refresh();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //栈内复用
        //登录界面跳转过来
        setIntent(intent);
        initUser();
    }

    @Override
    public void onReadUser() {
        User user = User.getInstance();
        initAvatar();
        getUser(user.getToken());
    }

    public void getUser(String token) {
        ((SystemPresenterImp) presenter).getUser(token);
    }

    private void initAvatar() {
        UserInfo.UserBean bean = User.getInstance().getInfo().getUser();
        tvUserName.setText(bean.getName());
        if (!"".equals(bean.getAvatar()) && bean.getAvatar() != null) {
            Glide.with(this)
                    .load(bean.getAvatar())
                    .placeholder(R.drawable.default_avatar)
                    .transform(new GlideGircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(navAvatar);
            Glide.with(this)
                    .load(bean.getAvatar())
                    .placeholder(R.drawable.default_avatar)
                    .transform(new GlideGircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(ivAvatar);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (viewPager.getCurrentItem()) {
            case 0:
                nav.setCheckedItem(R.id.menu_collected);
                break;
            case 1:
            default:
                nav.setCheckedItem(R.id.menu_index);
                break;
        }
        initUser();
    }

    private void changeSource() {
        int sourcePos = 0;
        final String[] array = getResources().getStringArray(R.array.source_short);
        String source = sp.getString("source", ApiManager.SOURCE_DMZJ);
        for (int index = 0; index < array.length; index++) {
            if (source.equals(array[index])) {
                sourcePos = index;
                break;
            }
        }
        final int finalSourcePos = sourcePos;
        sourceDialog = new AlertDialog.Builder(this)
                .setTitle("请选择数据源")
                .setSingleChoiceItems(R.array.source_all, sourcePos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which != finalSourcePos) {
                            sp.edit().putString("source", array[which]).apply();
                            //刷新HomeFragment和CategoryFragment
                            tvSource.setText(getResources().getStringArray(R.array.source_all)[which]);
                            ((HomeFragment) fragments.get(1)).getData();
                            ((CategoryFragment) fragments.get(2)).getCategory();
                        }
                        sourceDialog.dismiss();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void clearCache() {
        //清除缓存
        cachePresenterImp.clearCache();
    }

    private void calculateCache() {
        cachePresenterImp.calculateCacheSize();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermission() {
        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            requestMultiplePermissions();
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestMultiplePermissions() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions, REQUEST_PERMISSION_CODE);
    }

    private void selectedIcon(TabLayout.Tab tab) {
        tab.setIcon(selectedPics[tab.getPosition()]);
    }

    private void unselectedIcon(TabLayout.Tab tab) {
        tab.setIcon(tabPics[tab.getPosition()]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                if (sp.getBoolean("isAutoClearCache", false)) {
                    clearCache();
                } else {
//                    System.exit(0);
                    BaseApplication.getActivityHelper().finishAllActivity();
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onGetUser(UserInfo info) {
        //更新用户数据
        User user = User.getInstance();
        user.saveUser(info);
        refreshCollection();
        initAvatar();
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.e("登录过期");
        sp.edit().putBoolean("isLogin", false).apply();
        User.getInstance().clearUser();
    }

    @Override
    public void onClearCacheComplete() {
        Logger.d("清除成功");
//        System.exit(0);
        BaseApplication.getActivityHelper().finishAllActivity();
    }

    @Override
    public void onCalculateCache(long totalSize) {
        Logger.d("缓存大小为" + totalSize / 1024 / 1024 + "MB");
        /*if (totalSize / 1024 / 1024 > 200) {
            clearCache();
        } else {
            System.exit(0);
        }*/
    }
}
