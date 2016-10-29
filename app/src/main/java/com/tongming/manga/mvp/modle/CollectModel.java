package com.tongming.manga.mvp.modle;

import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseModel;
import com.tongming.manga.mvp.bean.CollectedComic;
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
 * Created by Tongming on 2016/8/17.
 */
public class CollectModel extends BaseModel implements ICollectModel {

    private OnCollectListener collectListener;

    public CollectModel(OnCollectListener collectListener) {
        this.collectListener = collectListener;
        manager = DBManager.getInstance();
    }

    @Override
    public Subscription queryAllCollect() {
        return manager.queryAllCollected()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CollectedComic>>() {
                    @Override
                    public void onCompleted() {
                        this.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        collectListener.onFail(e);
                        this.unsubscribe();
                    }

                    @Override
                    public void onNext(List<CollectedComic> comics) {
                        collectListener.onQueryAllCompleted(comics);
                        this.unsubscribe();
                    }
                });
    }

    @Override
    public void deleteCollectByName(String name) {
        int state = manager.deleteCollectByName(name);
        collectListener.onDeleteByName(state);

    }

    @Override
    public void deleteAllCollect() {
        int state = manager.deleteAllCollect();
        collectListener.onDeleteAll(state);
    }

    @Override
    public Subscription deleteCollectOnNet(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getToken());
        map.put("name", name);
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
                        collectListener.onFail(e);
                    }

                    @Override
                    public void onNext(UserInfo info) {
                        collectListener.onDeleteOnNet(info);
                    }
                });
    }

    public interface OnCollectListener {
        void onQueryAllCompleted(List<CollectedComic> comics);

        void onDeleteByName(int state);

        void onDeleteAll(int state);

        void onDeleteOnNet(UserInfo info);

        void onFail(Throwable throwable);
    }
}
