package com.tongming.manga.mvp.modle;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.MangaToken;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

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


    public LoginModel(onLoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    public Subscription login(String phone, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", password);
        JSONObject object = new JSONObject(map);
        RequestBody body = RequestBody.create(ApiManager.JSON, object.toString());
        return ApiManager.getInstance()
                .login(body)
                .flatMap(new Func1<MangaToken, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(MangaToken token) {
                        //将token保存
                        User.getInstance().setToken(token.getToken());
                        return ApiManager.getInstance()
                                .getUserInfo(token.getToken());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserInfo>() {
                    @Override
                    public void call(UserInfo info) {
                        Logger.d("登录成功");
                        User.getInstance().setInfo(info);
                        loginListener.onLogin(info);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.d(throwable.getMessage());
                        loginListener.onFail(throwable);
                    }
                });
    }

    @Override
    public void saveUser(final User user) {
        Logger.d("saveUser");
        //将User对象保存到本地
        new Thread(new Runnable() {
            File file = new File(BaseApplication.getContext().getFilesDir() + "/data.dat");

            @Override
            public void run() {
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(user);
                    loginListener.onSaveUser(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    loginListener.onSaveUser(false);
                    Logger.d("保存失败" + e.getMessage());
                } finally {
                    try {
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
                            Logger.d("保存成功");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        ).start();
    }

    public interface onLoginListener {
        void onLogin(UserInfo info);

        void onSaveUser(boolean result);

        void onFail(Throwable throwable);
    }
}
