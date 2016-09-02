package com.tongming.manga.mvp.modle;

import rx.Subscription;

/**
 * Created by Tongming on 2016/9/1.
 */
public interface IPersonModel {
    Subscription updateUser(String nickname, String sex, String personality);

    Subscription updateUser(String path, String nickname, String sex, String personality);
}
