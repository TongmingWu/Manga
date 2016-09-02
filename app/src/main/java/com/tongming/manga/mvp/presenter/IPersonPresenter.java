package com.tongming.manga.mvp.presenter;

/**
 * Created by Tongming on 2016/9/1.
 */
public interface IPersonPresenter {
    void updateUser(String nickname, String sex, String personality);

    void updateUser(String path, String nickname, String sex, String personality);
}
