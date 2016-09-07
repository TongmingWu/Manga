package com.tongming.manga.mvp.presenter;

import java.io.File;

/**
 * Created by Tongming on 2016/9/1.
 */
interface IPersonPresenter {
    void updateUser(String nickname, String sex, String personality);

    void updateUser(File file, String nickname, String sex, String personality);

    void compressFile(String path);
}
