package com.tongming.manga.mvp.bean;

/**
 * Created by Tongming on 2016/8/17.
 */
public class CollectedComic {
    @Override
    public String toString() {
        return "CollectedComic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", area='" + area + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", lastTime=" + lastTime +
                ", url='" + url + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }

    private int id;
    private String name;
    private String author;
    private String area;
    private String type;
    private int status;
    private long lastTime;
    private String url;
    private String cover;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getId() {

        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getArea() {
        return area;
    }

    public String getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public long getLastTime() {
        return lastTime;
    }

    public String getUrl() {
        return url;
    }

    public String getCover() {
        return cover;
    }
}
