package com.tongming.manga.mvp.bean;

import com.orhanobut.logger.Logger;
import com.tongming.manga.mvp.base.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Tongming on 2016/8/20.
 */
public class User implements Serializable {

    private static final long serialVersionUID = -21455356667888L;
    private static User instance;
    private String token;
    private UserInfo info;

    private User() {
    }

    public static synchronized User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    public String getToken() {
        return token;
    }

    public UserInfo getInfo() {
        return info;
    }

    public void saveUser(UserInfo info) {
        Logger.d(info.getUser().toString());
        //保存到本地
        instance.setInfo(info);
        new Thread(new Runnable() {
            File file = new File(BaseApplication.getContext().getFilesDir() + "/data.dat");

            @Override
            public void run() {
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(instance);
                    Logger.d("保存成功");
                } catch (IOException e) {
                    e.printStackTrace();
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

    public void clearUser() {
        instance.setInfo(null);
        instance.setToken(null);
        new Thread(new Runnable() {
            File file = new File(BaseApplication.getContext().getFilesDir() + "/data.dat");

            @Override
            public void run() {
                if (file.exists()) {
                    file.delete();
                }
            }
        }).start();
    }

    private User readUser() {

        return instance;
    }
}
