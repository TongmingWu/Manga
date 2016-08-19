package com.tongming.manga.mvp.bean;

/**
 * Created by Tongming on 2016/8/15.
 */
public class HistoryComic {
    private int id;
    private String name;
    private String author;
    private String type;
    private String area;
    private String url;
    private String historyName;
    private String historyUrl;
    private String cover;
    private long lastTime;
    private int status;
    private int chapterNum;

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastTime() {

        return lastTime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setHistoryUrl(String historyUrl) {
        this.historyUrl = historyUrl;
    }

    public String getHistoryUrl() {

        return historyUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setHistoryName(String historyName) {
        this.historyName = historyName;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setChapterNum(int chapterNum) {
        this.chapterNum = chapterNum;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    public String getHistoryName() {
        return historyName;
    }

    public String getCover() {
        return cover;
    }

    public int getStatus() {
        return status;
    }

    public int getChapterNum() {
        return chapterNum;
    }

    @Override
    public String toString() {
        return "HistoryComic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", area='" + area + '\'' +
                ", url='" + url + '\'' +
                ", historyName='" + historyName + '\'' +
                ", historyUrl='" + historyUrl + '\'' +
                ", cover='" + cover + '\'' +
                ", lastTime=" + lastTime +
                ", status=" + status +
                ", chapterNum=" + chapterNum +
                '}';
    }
}
