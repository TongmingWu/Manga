package com.tongming.manga.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by Tongming on 2016/8/9.
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment {
    private View rootView;
    protected BasePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), null);
        }
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.clearSubscription();
        }
    }
}
