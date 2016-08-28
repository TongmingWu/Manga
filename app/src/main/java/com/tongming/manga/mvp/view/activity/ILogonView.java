package com.tongming.manga.mvp.view.activity;

/**
 * Created by Tongming on 2016/8/28.
 */
public interface ILogonView {
    void onGetCode(boolean result);

    void onLogon(boolean result);

    void showDialog();

    void hideDialog();

    void onFail(Throwable throwable);
}
