package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.modle.ISystemModel;
import com.tongming.manga.mvp.modle.SystemModel;
import com.tongming.manga.mvp.view.activity.ISystemView;

/**
 * Created by Tongming on 2016/8/14.
 */
public class SystemPresenterImp extends BasePresenter implements ISystemPresenter, SystemModel.onCompleteListener {

    private ISystemView systemView;
    private ISystemModel systemModel;

    public SystemPresenterImp(ISystemView systemView) {
        this.systemView = systemView;
        systemModel = new SystemModel(this);
    }

    @Override
    public void getUser(String token) {
        addSubscription(systemModel.getUser(token));
    }

    @Override
    public void readUser() {
        systemModel.readUser();
    }

    @Override
    public void onGetUser(UserInfo info) {
        systemView.onGetUser(info);
    }

    @Override
    public void onReadUser() {
        systemView.onReadUser();
    }

    @Override
    public void onFail(Throwable throwable) {
        systemView.onFail(throwable);
    }
}
