package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.bean.User;

/**
 * Created by Tongming on 2016/8/28.
 */
interface ILoginPresenter {
    void login(String phone, String password);

    void saveUser(User user);
}
