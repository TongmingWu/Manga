package com.tongming.manga.mvp.presenter;

/**
 * Created by Tongming on 2016/8/28.
 */
interface ILogonPresenter {
    void getCode(String phone);

    void logon(String phone, String pwd, String code);
}
