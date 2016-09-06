package com.tongming.manga.mvp.modle;

import java.io.File;

import rx.Subscription;

/**
 * Created by Tongming on 2016/9/1.
 */
public interface IPersonModel {
    Subscription updateUser(String nickname, String sex, String personality);

    Subscription updateUser(File file, String nickname, String sex, String personality);

    Subscription compressFile(String path);
}
