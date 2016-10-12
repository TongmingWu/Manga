package com.tongming.manga.mvp.bean;

/**
 * Author: Tongming
 * Date: 2016/10/10
 */

public class SearchRecord {

    private String comic_name;
    private String comic_url;
    private int last_time;

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }

    public int getLast_time() {

        return last_time;
    }

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public void setComic_url(String comic_url) {
        this.comic_url = comic_url;
    }

    public String getComic_name() {

        return comic_name;
    }

    public String getComic_url() {
        return comic_url;
    }
}
