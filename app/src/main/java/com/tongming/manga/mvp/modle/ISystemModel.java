package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/14.
 */
public interface ISystemModel {

    Subscription getUser(String token);

    void readUser();
}
