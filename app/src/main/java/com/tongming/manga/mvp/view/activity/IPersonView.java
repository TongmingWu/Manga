package com.tongming.manga.mvp.view.activity;

import com.tongming.manga.mvp.bean.UserInfo;

import java.io.File;

/**
 * Created by Tongming on 2016/9/1.
 */
public interface IPersonView {
    void onUpdateUser(UserInfo info);

    void onCompress(File file);

    void onFail(Throwable throwable);
}
