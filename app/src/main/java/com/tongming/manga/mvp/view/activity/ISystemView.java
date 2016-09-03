package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.UserInfo;

/**
 * Created by Tongming on 2016/8/14.
 */
public interface ISystemView {

    void onGetUser(UserInfo info);

    void onReadUser();

    void onFail(Throwable throwable);
}
