package com.tongming.manga.mvp.modle;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.api.ApiManager;
import com.tongming.manga.mvp.bean.Result;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Tongming on 2016/8/28.
 */
public class LogonModel implements ILogonModel {

    private onLogonListener logonListener;


    public LogonModel(onLogonListener logonListener) {
        this.logonListener = logonListener;
    }

    @Override
    public Subscription getCode(String phone) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("appId", ApiManager.APP_ID);
        map.put("appKey", ApiManager.APP_KEY);
        JSONObject object = new JSONObject(map);
        String json = object.toString();
        RequestBody body = RequestBody.create(ApiManager.JSON, json);
        return ApiManager.getInstance().requestSms(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        if (result.getCode() != 200) {
                            logonListener.onGetCode(false);
                        } else {
                            logonListener.onGetCode(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        logonListener.onFail(throwable);
                    }
                });
    }

    @Override
    public Subscription logon(String phone, String pwd, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("password", pwd);
        map.put("code", code);
        map.put("appId", ApiManager.APP_ID);
        map.put("appKey", ApiManager.APP_KEY);
        JSONObject object = new JSONObject(map);
        String json = object.toString();
        RequestBody body = RequestBody.create(ApiManager.JSON, json);
        return ApiManager.getInstance()
                .logon(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        if (result.getCode() != 200) {
                            Logger.d("注册失败");
                            logonListener.onLogon(false);
                        } else {
                            Logger.d("注册成功");
                            logonListener.onLogon(true);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        logonListener.onFail(throwable);
                    }
                });
    }

    public interface onLogonListener {
        void onGetCode(boolean result);

        void onLogon(boolean result);

        void onFail(Throwable throwable);
    }
}
