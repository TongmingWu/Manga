package com.tongming.manga.mvp.modle;

import android.content.Context;
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

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/14.
 */
public class SystemModel implements ISystemModel {

    private onCompleteListener onCompleteListener;
    private long totalSize;

    public SystemModel(SystemModel.onCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    @Override
    public Subscription clearCache(Context context, boolean clearAll) {
        File file = new File(context.getCacheDir() + "/image_manager_disk_cache/");
        Subscriber<File> subscriber = new Subscriber<File>() {
            //清除全部
            @Override
            public void onCompleted() {
                onCompleteListener.onClearCacheCompleted();
            }

            @Override
            public void onError(Throwable e) {
                onCompleteListener.onFail(e);
            }

            @Override
            public void onNext(File file) {
                file.delete();
            }
        };
        return Observable.from(file.listFiles())
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.isFile();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public Subscription calculateSize(Context context) {
        File file = new File(context.getCacheDir() + "/image_manager_disk_cache/");
        Subscriber<File> subscriber = new Subscriber<File>() {
            @Override
            public void onCompleted() {
                onCompleteListener.onCalculateCompleted(totalSize);
            }

            @Override
            public void onError(Throwable e) {
                onCompleteListener.onFail(e);
            }

            @Override
            public void onNext(File file) {
                totalSize += file.length();
            }
        };
        return Observable.from(file.listFiles())
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return file.isFile();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    @Override
    public Subscription getUser(String token) {
        return ApiManager.getInstance()
                .getUserInfo(token)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo info) {
                        onCompleteListener.onGetUser(info);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onCompleteListener.onFail(throwable);
                    }
                });
    }

    @Override
    public void readUser() {
        Logger.d("readUser");
        //从本地读取User
        new Thread(new Runnable() {
            File file = new File(BaseApplication.getContext().getFilesDir() + "/data.dat");

            @Override
            public void run() {
                ObjectInputStream ois = null;
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
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface onCompleteListener {
        void onClearCacheCompleted();

        void onFail(Throwable throwable);

        void onCalculateCompleted(long size);

        void onGetUser(UserInfo info);

        void onReadUser();
    }
}
