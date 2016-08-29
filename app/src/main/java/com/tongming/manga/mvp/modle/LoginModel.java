package com.tongming.manga.mvp.modle;

import android.content.Context;
import android.content.SharedPreferences;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.MangaToken;
import com.tongming.manga.mvp.bean.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LoginModel implements ILoginModel {

    private onLoginListener loginListener;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public LoginModel(onLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    public Subscription login(String phone, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        JSONObject object = new JSONObject(map);
        RequestBody body = RequestBody.create(JSON, object.toString());
        return ApiManager.getInstance()
                .login(body)
                .flatMap(new Func1<MangaToken, Observable<User>>() {
                    @Override
                    public Observable<User> call(MangaToken token) {
                        //将token保存
                        SharedPreferences sp = BaseApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                        sp.edit().putString("token", token.getToken()).apply();
                        return ApiManager.getInstance()
                                .getUserInfo(token.getToken());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        Logger.d("登录成功");
                        loginListener.onLogin(user);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.d("登录失败");
                        loginListener.onFail(throwable);
                    }
                });
    }

    public interface onLoginListener {
        void onLogin(User user);

        void onFail(Throwable throwable);
    }
}
