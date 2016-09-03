package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.base.BaseApplication;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/9/3.
 */
public class CacheModel implements ICacheModel {

    private onCacheListener cacheListener;
    private long totalSize;

    public CacheModel(onCacheListener cacheListener) {
        this.cacheListener = cacheListener;
    }

    @Override
    public Subscription clearCache() {
        File file = new File(BaseApplication.getContext().getExternalCacheDir() + "/image_manager_disk_cache/");
        Subscriber<File> subscriber = new Subscriber<File>() {
            //清除全部
            @Override
            public void onCompleted() {
                cacheListener.onClearCacheComplete();
            }

            @Override
            public void onError(Throwable e) {
                cacheListener.onFail(e);
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
    public Subscription calculateSize() {
        File file = new File(BaseApplication.getContext().getExternalCacheDir() + "/image_manager_disk_cache/");
        Subscriber<File> subscriber = new Subscriber<File>() {
            @Override
            public void onCompleted() {
                cacheListener.onCalculateComplete(totalSize);
            }

            @Override
            public void onError(Throwable e) {
                cacheListener.onFail(e);
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

    public interface onCacheListener {
        void onClearCacheComplete();

        void onCalculateComplete(long totalSize);

        void onFail(Throwable e);
    }
}
