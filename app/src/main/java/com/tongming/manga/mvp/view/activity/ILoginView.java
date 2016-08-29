package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.User;

/**
 * Created by Tongming on 2016/8/28.
 */
public interface ILoginView {
    void onLogin(User user);

    void showDialog();

    void hideDialog();

    void onFail(Throwable throwable);
}
