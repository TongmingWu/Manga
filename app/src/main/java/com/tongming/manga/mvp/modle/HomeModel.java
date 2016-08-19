package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.Hot;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/9.
 */
public class HomeModel implements IHomeModel {

    private onGetDataListener mOnGetDataListener;

    public HomeModel(onGetDataListener mOnGetDataListener) {
        this.mOnGetDataListener = mOnGetDataListener;
    }

    @Override
    public Subscription getData() {
        return ApiManager.getInstance().getHot()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Hot>() {
                               @Override
                               public void call(Hot hot) {
                                   mOnGetDataListener.onSuccess(hot);
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable throwable) {
                                   mOnGetDataListener.onFail(throwable);
                               }
                           }
                );
    }

    public interface onGetDataListener {
        void onSuccess(Hot hot);

        void onFail(Throwable throwable);
    }
}
