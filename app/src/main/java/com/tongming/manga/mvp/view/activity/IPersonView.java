package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.UserInfo;

/**
 * Created by Tongming on 2016/9/1.
 */
public interface IPersonView {
    void onUpdateUser(UserInfo info);

    void onFail(Throwable throwable);
}
