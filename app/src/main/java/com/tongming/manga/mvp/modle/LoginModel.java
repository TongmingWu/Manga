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
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LoginModel implements ILoginModel {

    private onLoginListener loginListener;
    private boolean isSuccess = false;


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
        Subscription subscribe = ApiManager.getInstance()
                .login(body)
                .filter(new Func1<MangaToken, Boolean>() {
                    @Override
                    public Boolean call(MangaToken mangaToken) {
                        return mangaToken.getCode() == 200;
                    }
                })
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
                .subscribe(new Subscriber<UserInfo>() {
                    @Override
                    public void onCompleted() {
                        if (!isSuccess) {
                            loginListener.onFail(new Throwable("登录失败"));
                        }
                        Logger.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d(e.getMessage());
                        loginListener.onFail(e);
                    }

                    @Override
                    public void onNext(UserInfo info) {
                        isSuccess = true;
                        Logger.d("登录成功");
                        User.getInstance().setInfo(info);
                        loginListener.onLogin(info);
                    }
                });
        return subscribe;
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
                    Logger.d("保存成功");
                } catch (IOException e) {
                    e.printStackTrace();
                    loginListener.onSaveUser(false);
                    Logger.d("保存失败" + e.getMessage());
                } finally {
                    try {
                        if (objectOutputStream != null) {
                            objectOutputStream.close();
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
