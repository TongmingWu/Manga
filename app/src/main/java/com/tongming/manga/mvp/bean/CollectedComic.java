package com.tongming.manga.mvp.bean;

import java.io.Serializable;

/**
 * Created by Tongming on 2016/8/17.
 */
public class CollectedComic implements Serializable {
    private static final long serialVersionUID = -21455356667888L;
    private String comic_name;
    private String comic_author;
    private String comic_area;
    private String comic_category;
    private int status;
    private long lastTime;
    private String comic_source;
    private String comic_url;
    private String comic_id;
    private String cover;

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public void setComic_author(String comic_author) {
        this.comic_author = comic_author;
    }

    public void setComic_area(String comic_area) {
        this.comic_area = comic_area;
    }

    public void setComic_category(String comic_category) {
        this.comic_category = comic_category;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setComic_source(String comic_source) {
        this.comic_source = comic_source;
    }

    public void setComic_url(String comic_url) {
        this.comic_url = comic_url;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getComic_name() {
        return comic_name;
    }

    public String getComic_author() {
        return comic_author;
    }

    public String getComic_area() {
        return comic_area;
    }

    public String getComic_category() {
        return comic_category;
    }

    public int getStatus() {
        return status;
    }

    public long getLastTime() {
        return lastTime;
    }

    public String getComic_source() {
        return comic_source;
    }

    public String getComic_url() {
        return comic_url;
    }

    public String getComic_id() {
        return comic_id;
    }

    public String getCover() {
        return cover;
    }

    @Override
    public String toString() {
        return "CollectedComic{" +
                "comic_name='" + comic_name + '\'' +
                ", comic_author='" + comic_author + '\'' +
                ", comic_area='" + comic_area + '\'' +
                ", comic_category='" + comic_category + '\'' +
                ", status=" + status +
                ", lastTime=" + lastTime +
                ", comic_source='" + comic_source + '\'' +
                ", comic_url='" + comic_url + '\'' +
                ", comic_id='" + comic_id + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
