package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/8/28.
 */
public interface ILogonModel {
    Subscription getCode(String phone);

    Subscription logon(String phone, String pwd, String code);
}
