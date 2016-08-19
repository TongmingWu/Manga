package com.tongming.manga.mvp.modle;

import android.content.Context;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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
        //TODO 设置缓存上限,根据上限清除部分缓存
        Subscriber<File> subscriber = new Subscriber<File>() {
            //清除全部
            @Override
            public void onCompleted() {
                onCompleteListener.onClearCacheCompleted();
            }

            @Override
            public void onError(Throwable e) {
                onCompleteListener.onFail();
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
                onCompleteListener.onFail();
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

    public interface onCompleteListener {
        void onClearCacheCompleted();

        void onFail();

        void onCalculateCompleted(long size);
    }
}
