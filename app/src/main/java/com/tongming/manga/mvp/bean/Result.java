package com.tongming.manga.mvp.bean;

/**
 * Created by Tongming on 2016/8/20.
 */
public class Result {

    private int code;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {

        return code;
    }

    public String getMessage() {
        return message;
    }
}
