package com.tongming.manga.mvp.modle;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.CollectedComic;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.HistoryComic;
import com.tongming.manga.mvp.bean.Result;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.mvp.db.DBManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/11.
 */
public class DetailModel implements IDetailModel {
    private onGetDataListener onGetDataListener;

    public DetailModel(DetailModel.onGetDataListener onGetDataListener) {
        this.onGetDataListener = onGetDataListener;
    }

    @Override
    public Subscription getDetail(String source, String comicUrl) {
        return ApiManager.getInstance().getComicInfo(source, comicUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ComicInfo>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetDataListener.onFail(e);
                    }

                    @Override
                    public void onNext(ComicInfo info) {
                        onGetDataListener.onGetData(info);
                    }
                });
    }

    @Override
    public void addHistory(ComicInfo info, String historyName, String historyUrl) {
        DBManager manager = DBManager.getInstance();
        long state = manager.insertHistory(info, historyName, historyUrl);
        manager.closeDB();
        onGetDataListener.onAddHistoryCompleted(state);
    }

    @Override
    public void updateHistory(ComicInfo info, String historyName, String historyUrl) {
        DBManager manager = DBManager.getInstance();
        if (!TextUtils.isEmpty(historyName) && !TextUtils.isEmpty(historyUrl)) {
            int state = manager.updateHistory(info, historyName, historyUrl);
            manager.closeDB();
            onGetDataListener.onUpdateHistoryCompleted(state);
        }
    }

    @Override
    public Subscription queryHistoryByName(final String comicName) {
        Logger.d("开始读取历史记录");
        final DBManager manager = DBManager.getInstance();
        return manager.queryHistoryByName(comicName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HistoryComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                        manager.closeDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetDataListener.onFail(e);
                        manager.closeDB();
                    }

                    @Override
                    public void onNext(List<HistoryComic> comics) {
                        if (comics.size() > 0) {
                            Logger.d("得到的历史记录:" + comics.get(0).getHistoryUrl());
                            for (HistoryComic comic : comics) {
                                if (!TextUtils.isEmpty(comic.getHistoryUrl())) {
                                    onGetDataListener.onQueryHistoryCompleted(
                                            comic.getHistoryName(),
                                            comic.getHistoryUrl());
                                }
                            }
                        } else {
                            onGetDataListener.onQueryHistoryCompleted("", "");
                        }
                        manager.closeDB();
                        this.unsubscribe();
                    }
                });
    }

    @Override
    public void collectComic(final ComicInfo info) {
        DBManager manager = DBManager.getInstance();
        long state = manager.collectComic(info);
        manager.closeDB();
        onGetDataListener.onAddCollectCompleted(state);
    }

    @Override
    public Subscription queryCollectByName(String name) {
        final DBManager manager = DBManager.getInstance();
        return manager.queryCollectedByName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                        manager.closeDB();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetDataListener.onFail(e);
                        manager.closeDB();
                    }

                    @Override
                    public void onNext(List<CollectedComic> comics) {
                        if (comics.size() > 0) {
                            onGetDataListener.onQueryCollectCompleted(true);
                        }
                    }
                });
    }

    @Override
    public void deleteCollectByName(String name) {
        DBManager manager = DBManager.getInstance();
        int state = manager.deleteCollectByName(name);
        manager.closeDB();
        onGetDataListener.onDeleteCollectCompleted(state);
    }

    @Override
    public Subscription collectComicOnNet(ComicInfo info) {
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getToken());
        map.put("name", info.getComic_name());
        map.put("author", info.getComic_author());
        map.put("area", info.getComic_area());
        map.put("category", info.getComic_type());
        map.put("url", info.getComic_url());
        int status = info.getStatus().contains("连载") ? 0 : 1;
        map.put("status", status + "");
        map.put("cover", info.getCover());
        map.put("comic_source", info.getComic_source());
        JSONObject object = new JSONObject(map);
        RequestBody body = RequestBody.create(ApiManager.JSON, object.toString());
        return ApiManager.getInstance().addCollection(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("添加收藏失败");
                        onGetDataListener.onFail(e);
                    }

                    @Override
                    public void onNext(UserInfo info) {
                        onGetDataListener.onAddCollectOnNet(info);
                    }
                });
    }

    @Override
    public Subscription queryCollectOnNet(String name) {
        return ApiManager.getInstance()
                .queryCollection(User.getInstance().getInfo().getUser().getUid(), name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("读取收藏失败");
                        onGetDataListener.onFail(e);
                    }


                    @Override
                    public void onNext(Result result) {
                        if (result.getCode() == 200) {
                            onGetDataListener.onQueryCollectOnNet(true);
                        } else {
                            onGetDataListener.onQueryCollectOnNet(false);
                        }
                    }
                });
    }

    @Override
    public Subscription deleteCollectOnNet(ComicInfo info) {
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getToken());
        map.put("name", info.getComic_name());
        JSONObject object = new JSONObject(map);
        RequestBody body = RequestBody.create(ApiManager.JSON, object.toString());
        return ApiManager.getInstance().deleteCollection(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetDataListener.onFail(e);
                    }

                    @Override
                    public void onNext(UserInfo info) {
                        onGetDataListener.onDeleteCollectOnNet(info);
                    }
                });
    }

    public interface onGetDataListener {
        void onGetData(ComicInfo info);

        void onAddHistoryCompleted(long state);

        void onUpdateHistoryCompleted(int state);

        void onQueryHistoryCompleted(String historyName, String historyUrl);

        void onAddCollectCompleted(long state);

        void onQueryCollectCompleted(boolean isCollected);

        void onDeleteCollectCompleted(int state);

        void onQueryCollectOnNet(boolean isCollected);

        void onAddCollectOnNet(UserInfo info);

        void onDeleteCollectOnNet(UserInfo info);

        void onFail(Throwable throwable);
    }
}
