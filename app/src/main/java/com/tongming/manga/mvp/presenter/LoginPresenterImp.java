package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.modle.ILoginModel;
import com.tongming.manga.mvp.modle.LoginModel;
import com.tongming.manga.mvp.view.activity.ILoginView;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LoginPresenterImp extends BasePresenter implements ILoginPresenter, LoginModel.onLoginListener {

    private ILoginView loginView;
    private ILoginModel loginModel;

    public LoginPresenterImp(ILoginView loginView) {
        this.loginView = loginView;
        loginModel = new LoginModel(this);
    }

    @Override
    public void login(String phone, String password) {
        addSubscription(loginModel.login(phone, password));
        loginView.showDialog();
    }

    @Override
    public void saveUser(User user) {
        loginModel.saveUser(user);
    }

    @Override
    public void onLogin(UserInfo info) {
        loginView.onLogin(info);
        loginView.hideDialog();
    }

    @Override
    public void onSaveUser(boolean result) {
        loginView.onSaveUser(result);
    }

    @Override
    public void onFail(Throwable throwable) {
        loginView.onFail(throwable);
        loginView.hideDialog();
    }
}
