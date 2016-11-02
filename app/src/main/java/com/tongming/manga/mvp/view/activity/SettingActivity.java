package com.tongming.manga.mvp.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tongming.manga.R;
import com.tongming.manga.cusview.CustomCachingGlideModule;
import com.tongming.manga.mvp.base.SwipeBackActivity;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.presenter.CachePresenterImp;
import com.tongming.manga.util.CommonUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tongming on 2016/9/3.
 */
public class SettingActivity extends SwipeBackActivity implements ICacheView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cache_ceiling)
    TextView tvCacheCeiling;
    @BindView(R.id.rl_cache_ceiling)
    RelativeLayout rlCacheCeiling;
    @BindView(R.id.tv_cache_clear)
    TextView tvCacheClear;
    @BindView(R.id.rl_cache_clear)
    RelativeLayout rlCacheClear;
    @BindView(R.id.rl_cache_auto_clear)
    RelativeLayout rlCacheAutoClear;
    @BindView(R.id.sw_cache_auto_clear)
    Switch swCache;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    private SharedPreferences sp;
    private AlertDialog alertDialog;
    private long availableSize;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initToolbar(toolbar);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        int cacheCeiling = sp.getInt("cacheCeiling", CustomCachingGlideModule.DEFAULT_CACHE_CEILING);
        if (cacheCeiling != CustomCachingGlideModule.DEFAULT_CACHE_CEILING) {
            tvCacheCeiling.setText(cacheCeiling + "M");
        } else {
            tvCacheCeiling.setText(cacheCeiling / 1024 / 1024 + "M");
        }
        boolean isAutoClearCache = sp.getBoolean("isAutoClearCache", false);
        if (isAutoClearCache) {
            swCache.setChecked(true);
        } else {
            swCache.setChecked(false);
        }
        presenter = new CachePresenterImp(this);
        ((CachePresenterImp) presenter).calculateCacheSize();
        availableSize = CommonUtil.getAvailableSize() / 1024 / 1024;
        if (!sp.getBoolean("isLogin", false)) {
            tvLogout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rl_cache_ceiling, R.id.rl_cache_clear, R.id.rl_cache_auto_clear, R.id.tv_logout, R.id.rl_read_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_cache_ceiling:
                showEditDialog();
//                showEditDialogFragment();
                break;
            case R.id.rl_cache_clear:
                clearCache();
                break;
            case R.id.rl_cache_auto_clear:
                autoClearCache();
                break;
            case R.id.rl_read_setting:
                startActivity(new Intent(this, WatchSettingActivity.class));
                break;
            case R.id.tv_logout:
                //退出登录
                logout();
                break;
        }
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setMessage("确定退出登录?")
                .setPositiveButton("注销", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp.edit().putBoolean("isLogin", false).apply();
                        User.getInstance().clearUser();
                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void clearCache() {
        new AlertDialog.Builder(this)
                .setMessage("确定清除图片缓存?")
                .setPositiveButton("清除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((CachePresenterImp) presenter).clearCache();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void autoClearCache() {
        if (sp.getBoolean("isAutoClearCache", false)) {
            sp.edit().putBoolean("isAutoClearCache", false).apply();
            swCache.setChecked(false);
        } else {
            sp.edit().putBoolean("isAutoClearCache", true).apply();
            swCache.setChecked(true);
        }
    }

    /*private void showEditDialogFragment() {
        FragmentManager fm = getFragmentManager();
        EditDialogFragment fragment = new EditDialogFragment();
        fragment.show(fm, "edit");
    }*/

    private void showEditDialog() {
        View inflate = View.inflate(SettingActivity.this, R.layout.dialog_cache, null);
        final EditText etCache = (EditText) inflate.findViewById(R.id.et_cache);
        etCache.setText(tvCacheCeiling.getText().toString().replace("M", ""));
        etCache.selectAll();
        final TextView tvTip = (TextView) inflate.findViewById(R.id.tv_cache_tip);
        etCache.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int size = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    size = Integer.parseInt(s.toString());
                }
                Logger.d("剩余存储=" + availableSize + "MB");
                if (size < 100) {
                    tvTip.setText("缓存大小不能小于100M");
                    tvTip.setTextColor(Color.RED);
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else if (size > availableSize) {
                    tvTip.setText("缓存大小不能超过剩余存储");
                    tvTip.setTextColor(Color.RED);
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    tvTip.setText("当前缓存上限为200M");
                    tvTip.setTextColor(getResources().getColor(R.color.gray, null));
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("自定义缓存")
                .setView(inflate)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cacheSize = etCache.getText().toString();
                        if (!TextUtils.isEmpty(cacheSize)) {
                            setCacheCeiling(Integer.parseInt(cacheSize));
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
        final Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window != null ? window.getAttributes() : null;
        params.gravity = Gravity.TOP;
        params.y = CommonUtil.getScreenHeight(this) / 4;    //监听软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();
    }

    private void setCacheCeiling(int size) {
        //以MB为单位,不能超过内置存储剩余容量,不小于100M
        sp.edit().putInt("cacheCeiling", size).apply();
        tvCacheCeiling.setText(size + "M");
        Toast.makeText(SettingActivity.this, "重启APP生效", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClearCacheComplete() {
        Toast.makeText(this, "清除成功", Toast.LENGTH_SHORT).show();
        tvCacheClear.setText(0 + "M");
    }

    @Override
    public void onCalculateCache(long totalSize) {
        tvCacheClear.setText(totalSize / 1024 / 1024 + "M");
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.d(throwable.getMessage());
    }
}
