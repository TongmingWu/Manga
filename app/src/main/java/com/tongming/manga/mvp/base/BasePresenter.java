package com.tongming.manga.mvp.base;

import com.orhanobut.logger.Logger;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Tongming on 2016/8/18.
 */
public class BasePresenter {

    private CompositeSubscription compositeSubscription;
    protected BaseModel baseModel;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    public void clearSubscription() {
        if (compositeSubscription != null && compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
            Logger.d("清除请求");
        }
    }

    public void closeDB() {
        if (baseModel != null) {
            baseModel.closeDB();
        }
    }
}
