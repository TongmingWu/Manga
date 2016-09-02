package com.tongming.manga.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tongming on 2016/8/17.
 */
public class CollectedComic implements Parcelable {
    private String name;
    private String author;
    private String area;
    private String type;
    private int status;
    private long lastTime;
    private String url;
    private String cover;


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

    @Override
    public String toString() {
        return "CollectedComic{" +
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.area);
        dest.writeString(this.type);
        dest.writeInt(this.status);
        dest.writeLong(this.lastTime);
        dest.writeString(this.url);
        dest.writeString(this.cover);
    }

    public CollectedComic() {
    }

    protected CollectedComic(Parcel in) {
        this.name = in.readString();
        this.author = in.readString();
        this.area = in.readString();
        this.type = in.readString();
        this.status = in.readInt();
        this.lastTime = in.readLong();
        this.url = in.readString();
        this.cover = in.readString();
    }

    public static final Parcelable.Creator<CollectedComic> CREATOR = new Parcelable.Creator<CollectedComic>() {
        @Override
        public CollectedComic createFromParcel(Parcel source) {
            return new CollectedComic(source);
        }

        @Override
        public CollectedComic[] newArray(int size) {
            return new CollectedComic[size];
        }
    };
}
