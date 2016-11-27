package com.tongming.manga.mvp.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.tongming.manga.util.CommonUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Tongming
 * Date: 2016/8/9
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected BasePresenter presenter;
    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        initView();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();



    protected void initToolbar(View view) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int height = CommonUtil.getScreenHeight(this);
        params.setMargins(0, height / 32, 0, 0);
        if (view instanceof Toolbar) {
//            setSupportActionBar((Toolbar) view);
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
        bind.unbind();
        if (presenter != null) {
            presenter.clearSubscription();
        }
    }
}
