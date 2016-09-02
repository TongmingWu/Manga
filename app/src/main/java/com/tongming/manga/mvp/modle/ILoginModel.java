package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.bean.User;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/28.
 */
public interface ILoginModel {

    Subscription login(String phone, String password);

    void saveUser(User user);
}
