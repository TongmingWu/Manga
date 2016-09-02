package com.tongming.manga.mvp.presenter;

import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.modle.IPersonModel;
import com.tongming.manga.mvp.modle.PersonModel;
import com.tongming.manga.mvp.view.activity.IPersonView;

/**
 * Created by Tongming on 2016/9/1.
 */
public class PersonPresenterImp extends BasePresenter implements IPersonPresenter, PersonModel.onUpdateListener {

    private IPersonView personView;
    private IPersonModel personModel;

    public PersonPresenterImp(IPersonView personView) {
        this.personView = personView;
        personModel = new PersonModel(this);
    }

    @Override
    public void updateUser(String nickname, String sex, String personality) {
        addSubscription(personModel.updateUser(nickname, sex, personality));
    }

    @Override
    public void updateUser(String path, String nickname, String sex, String personality) {
        addSubscription(personModel.updateUser(path, nickname, sex, personality));
    }

    @Override
    public void onUpdate(UserInfo info) {
        personView.onUpdateUser(info);
    }

    @Override
    public void onFail(Throwable throwable) {
        personView.onFail(throwable);
    }
}
