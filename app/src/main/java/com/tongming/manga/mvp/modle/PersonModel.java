package com.tongming.manga.mvp.modle;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/9/1.
 */
public class PersonModel implements IPersonModel {

    private onUpdateListener updateListener;

    public PersonModel(onUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    @Override
    public Subscription updateUser(String nickname, String sex, String personality) {

        return getNormalObservable(nickname, sex, personality)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo info) {
                        updateListener.onUpdate(info);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        updateListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription updateUser(String path, String nickname, String sex, String personality) {
        File file = new File(path);
        Logger.d(file.length() / 1024 + "kb");
        //TODO 压缩图片
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part data = MultipartBody.Part.createFormData("avatar", file.getName(), body);
        Observable<UserInfo> updateUser = getNormalObservable(nickname, sex, personality);
        Observable<UserInfo> uploadAvatar = ApiManager.getInstance().uploadAvatar(data);
        return Observable.merge(updateUser, uploadAvatar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserInfo>() {
                    UserInfo info = null;

                    @Override
                    public void onCompleted() {
                        updateListener.onUpdate(info);
                    }

                    @Override
                    public void onError(Throwable e) {
                        updateListener.onFail(e);
                    }

                    @Override
                    public void onNext(UserInfo info) {
                        this.info = info;
                    }
                });
    }

    private Observable<UserInfo> getNormalObservable(String nickname, String sex, String personality) {
        Map<String, String> map = new HashMap<>();
        map.put("token", User.getInstance().getToken());
        map.put("name", nickname);
        map.put("sex", sex);
        map.put("personality", personality);
        JSONObject object = new JSONObject(map);
        RequestBody body = RequestBody.create(ApiManager.JSON, object.toString());
        Observable<UserInfo> observable = ApiManager.getInstance().updateUser(body);
        return observable;
    }

    public interface onUpdateListener {
        void onUpdate(UserInfo info);

        void onFail(Throwable throwable);
    }
}
