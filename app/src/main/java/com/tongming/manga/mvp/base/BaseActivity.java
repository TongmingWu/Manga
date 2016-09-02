package com.tongming.manga.mvp.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tongming.manga.util.CommonUtil;

import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/9.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BasePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(getLayoutId());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ButterKnife.bind(this);
        initView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initToolbar(View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int height = CommonUtil.getDeviceHeight(this);
        params.setMargins(0, height / 32, 0, 0);
        if (view instanceof Toolbar) {
            setSupportActionBar((Toolbar) view);
            ((Toolbar) view).setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.clearSubscription();
        }
    }
}
