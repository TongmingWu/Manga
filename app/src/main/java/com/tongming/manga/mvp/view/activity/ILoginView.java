package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.UserInfo;

/**
 * Created by Tongming on 2016/8/28.
 */
public interface ILoginView {
    void onLogin(UserInfo info);

    void onSaveUser(boolean result);

    void showDialog();

    void hideDialog();

    void onFail(Throwable throwable);
}
