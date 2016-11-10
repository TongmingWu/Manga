package com.tongming.manga.mvp.modle;

import android.content.Context;
import android.text.TextUtils;

import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseModel;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.db.DBManager;
import com.tongming.manga.util.HeaderGlide;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/11.
 */
public class PageModel extends BaseModel implements IPageModel {

    private onGetPageListener onGetPageListener;
    private String source;

    public PageModel(PageModel.onGetPageListener onGetPageListener) {
        this.onGetPageListener = onGetPageListener;
        manager = DBManager.getInstance();
    }

    @Override
    public Subscription getPage(String source, String chapterUrl) {
        this.source = source;
        return ApiManager.getInstance().getComicPage(source, chapterUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ComicPage>() {
                    @Override
                    public void call(ComicPage page) {
                        onGetPageListener.onSuccess(page);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        onGetPageListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription cacheImg(final Context mContext, final List<String> imgList, final boolean isLast) {
        //判断是否为加载上一话,是的话reserve
        if (isLast) {
            Collections.reverse(imgList);
        }
        return Observable.from(imgList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if (isLast) {
                            Collections.reverse(imgList);
                        }
                        onGetPageListener.onPageCompleted(imgList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetPageListener.onFail(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            HeaderGlide.downloadImage(mContext, s, source);
                        }
                    }
                });
    }

    public interface onGetPageListener {
        void onSuccess(ComicPage page);

        void onPageCompleted(List<String> imgList);

        void onFail(Throwable throwable);
    }
}
