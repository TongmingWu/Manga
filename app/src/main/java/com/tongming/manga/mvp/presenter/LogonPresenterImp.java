package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.modle.ILogonModel;
import com.tongming.manga.mvp.modle.LogonModel;
import com.tongming.manga.mvp.view.activity.ILogonView;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LogonPresenterImp extends BasePresenter implements ILogonPresenter, LogonModel.onLogonListener {

    private ILogonView logonView;
    private ILogonModel logonModel;

    public LogonPresenterImp(ILogonView logonView) {
        this.logonView = logonView;
        logonModel = new LogonModel(this);
    }

    @Override
    public void getCode(String phone) {
        addSubscription(logonModel.getCode(phone));
    }

    @Override
    public void logon(String phone, String pwd, String code) {
        addSubscription(logonModel.logon(phone, pwd, code));
        logonView.showDialog();
    }

    @Override
    public void onGetCode(boolean result) {
        logonView.onGetCode(result);
//        clearSubscription();
    }

    @Override
    public void onLogon(boolean result) {
        logonView.onLogon(result);
        logonView.hideDialog();
    }

    @Override
    public void onFail(Throwable throwable) {
        logonView.onFail(throwable);
        logonView.hideDialog();
    }
}
