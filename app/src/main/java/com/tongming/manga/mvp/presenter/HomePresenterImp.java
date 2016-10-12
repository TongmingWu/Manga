package com.tongming.manga.mvp.presenter;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BasePresenter;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.modle.HomeModel;
import com.tongming.manga.mvp.view.activity.IHomeView;

/**
 * Created by Tongming on 2016/8/9.
 */
public class HomePresenterImp extends BasePresenter implements IHomePresenter, HomeModel.onGetDataListener {

    private IHomeView homeView;
    private HomeModel homeModel;

    public HomePresenterImp(IHomeView homeView) {
        homeModel = new HomeModel(this);
        this.homeView = homeView;
    }

    @Override
    public void getData() {
        addSubscription(homeModel.getData());
        homeView.showRefresh();
    }

    @Override
    public void onSuccess(Hot hot) {
//        homeView.hideRefresh();
        homeView.onLoad(hot);
    }

    @Override
    public void onFail(Throwable throwable) {
        Logger.e(throwable.getMessage());
        homeView.hideRefresh();
        homeView.onFail();
    }
}
