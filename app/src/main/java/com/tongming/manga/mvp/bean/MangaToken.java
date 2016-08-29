package com.tongming.manga.mvp.bean;

/**
 * Created by Tongming on 2016/8/29.
 */
public class MangaToken {
    private String token;
    private int code;
    private String message;

    public void setToken(String token) {
        this.token = token;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
