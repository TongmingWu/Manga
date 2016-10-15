package com.tongming.manga.mvp.bean;

/**
 * Author: Tongming
 * Date: 2016/9/15
 */

public class DownloadComic implements Comparable<DownloadComic> {
    private String comic_id;
    private int status;
    private String name;
    private String cover;
    private int count;
    private String comic_source;
    private String currentName;
    private int currentTotal;
    private int currentPosition;
    private int createTime;

    public void setComic_source(String comic_source) {
        this.comic_source = comic_source;
    }

    public String getComic_source() {

        return comic_source;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getComic_id() {

        return comic_id;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public void setCurrentTotal(int currentTotal) {
        this.currentTotal = currentTotal;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public String getCurrentName() {

        return currentName;
    }

    public int getCurrentTotal() {
        return currentTotal;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void addCount() {
        count++;
    }

    public int getCount() {

        return count;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getCover() {
        return cover;
    }

    @Override
    public int compareTo(DownloadComic comic) {
        return comic.getCreateTime() - this.getCreateTime();
    }
}
