package com.tongming.manga.mvp.modle;

import android.os.Handler;
import android.os.Looper;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/14.
 */
public class SystemModel implements ISystemModel {

    private onCompleteListener onCompleteListener;


    public SystemModel(SystemModel.onCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }


    @Override
    public Subscription getUser(String token) {
        return ApiManager.getInstance()
                .getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
//                        Logger.d("在线获取用户成功");
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onCompleteListener.onFail(e);
                    }

                    @Override
                    public void onNext(UserInfo info) {
                        onCompleteListener.onGetUser(info);
                    }
                });
    }

    @Override
    public void readUser() {
//        Logger.d("readUser");
        //从本地读取User
        new Thread(new Runnable() {
            File file = new File(BaseApplication.getContext().getFilesDir() + "/data.dat");

            @Override
            public void run() {
                ObjectInputStream ois;
                try {
                    if (file.exists()) {
                        ois = new ObjectInputStream(new FileInputStream(file));
                        User instance = User.getInstance();
                        User user = (User) ois.readObject();     //内存地址
                        instance.setToken(user.getToken());
                        instance.setInfo(user.getInfo());
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                onCompleteListener.onReadUser();
                            }
                        });
                    } else {
                        Logger.d("读取User失败");
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                onCompleteListener.onFail(new Throwable("读取User失败"));
                            }
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface onCompleteListener {

        void onFail(Throwable throwable);

        void onGetUser(UserInfo info);

        void onReadUser();
    }
}
