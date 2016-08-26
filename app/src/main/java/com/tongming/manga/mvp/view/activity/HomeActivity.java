package com.tongming.manga.mvp.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.mvp.base.BaseActivity;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.base.BaseFragment;
import com.tongming.manga.mvp.presenter.SystemPresenterImp;
import com.tongming.manga.mvp.view.fragment.CategoryFragment;
import com.tongming.manga.mvp.view.fragment.CollectionFragment;
import com.tongming.manga.mvp.view.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements ISystemView {

    private static final int REQUEST_PERMISSION_CODE = 1;
    private long exitTime = 0;
    private final int[] tabPics = {
            R.drawable.icon_tab_favor_us,
            R.drawable.icon_tab_recom_us,
            R.drawable.icon_tab_search_us
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

    @OnClick({R.id.iv_nav, R.id.iv_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_nav:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.iv_avatar:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }
        /*byte[] bytes = RSA.encryptData("哈哈".getBytes());
        if (bytes != null) {
            String strRead = Base64Utils.encode(bytes);
            Logger.d(strRead);
        }*/
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlBar.getLayoutParams();
        params.setMargins(0, 80, 0, 0);
        final List<BaseFragment> fragments = new ArrayList<>();
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
        tab.setupWithViewPager(viewPager);
        for (int i = 0; i < tabPics.length; i++) {
            tab.getTabAt(i).setIcon(tabPics[i]);
        }
        tab.getTabAt(1).select();
        tab.getTabAt(1).setIcon(selectedPics[1]);
        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        initDrawerView();
    }

    private void initDrawerView() {
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_index:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_local:
                        break;
                    case R.id.menu_history:
                        startActivityForResult(new Intent(HomeActivity.this, HistoryActivity.class), 1);
                        break;
                    case R.id.menu_collected:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_download:
                        break;
                    case R.id.menu_setting:
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
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
    }

    private void clearCache() {
        //清除缓存
        ((SystemPresenterImp) presenter).clearCache(BaseApplication.getContext(), false);
    }

    private void calculateCache() {
        presenter = new SystemPresenterImp(this);
        ((SystemPresenterImp) presenter).calculateCacheSize(BaseApplication.getContext());
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
                calculateCache();
                //System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClearCache() {
        Logger.d("清除成功");
        System.exit(0);
    }

    @Override
    public void onCalculateCacheSize(long size) {
        Logger.d("缓存大小为" + size / 1024 / 1024 + "MB");
        if (size / 1024 / 1024 > 200) {
            clearCache();
        } else {
            System.exit(0);
        }
    }
}
