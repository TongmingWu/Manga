package com.tongming.manga.server;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public class DownloadInfo implements Parcelable, Comparable {

    public static final int WAIT = 0;
    public static final int DOWNLOAD = 1;
    public static final int PAUSE = 2;
    public static final int COMPLETE = 3;
    public static final int STOP = 4;
    public static final int FAIL = 5;

    private int _id;
    private String comic_id;
    private String comic_name;
    private String comic_url;
    private String chapter_name;
    private String chapter_url;
    private String comic_source;
    private String cover;
    private int status;
    private int position;
    private int total;
    private int create_time;
    private int next;
    private int prepare;
    private String next_url;
    private String pre_url;

    public void setNext(int next) {
        this.next = next;
    }

    public void setPrepare(int prepare) {
        this.prepare = prepare;
    }

    public void setNext_url(String next_url) {
        this.next_url = next_url;
    }

    public void setPre_url(String pre_url) {
        this.pre_url = pre_url;
    }

    public int getNext() {

        return next;
    }

    public int getPrepare() {
        return prepare;
    }

    public String getNext_url() {
        return next_url;
    }

    public String getPre_url() {
        return pre_url;
    }

    public DownloadInfo() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public void setComic_url(String comic_url) {
        this.comic_url = comic_url;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public void setChapter_url(String chapter_url) {
        this.chapter_url = chapter_url;
    }

    public void setComic_source(String comic_source) {
        this.comic_source = comic_source;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public String getComic_id() {
        return comic_id;
    }

    public String getComic_name() {
        return comic_name;
    }

    public String getComic_url() {
        return comic_url;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public String getChapter_url() {
        return chapter_url;
    }

    public String getComic_source() {
        return comic_source;
    }

    public String getCover() {
        return cover;
    }

    public int getStatus() {
        return status;
    }

    public int getPosition() {
        return position;
    }

    public int getTotal() {
        return total;
    }

    public int getCreate_time() {
        return create_time;
    }

    @Override
    public int compareTo(Object o) {
        DownloadInfo info = (DownloadInfo) o;
        return info.create_time - this.create_time;
    }

    @Override
    public int hashCode() {
        return chapter_url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DownloadInfo)) {
            throw new ClassCastException("类型异常");
        }
        DownloadInfo info = (DownloadInfo) obj;
        return this.chapter_url.equals(info.chapter_url) && this.chapter_name.equals(info.chapter_name);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.comic_id);
        dest.writeString(this.comic_name);
        dest.writeString(this.comic_url);
        dest.writeString(this.chapter_name);
        dest.writeString(this.chapter_url);
        dest.writeString(this.comic_source);
        dest.writeString(this.cover);
        dest.writeInt(this.status);
        dest.writeInt(this.position);
        dest.writeInt(this.total);
        dest.writeInt(this.create_time);
        dest.writeInt(this.next);
        dest.writeInt(this.prepare);
        dest.writeString(this.next_url);
        dest.writeString(this.pre_url);
    }

    protected DownloadInfo(Parcel in) {
        this._id = in.readInt();
        this.comic_id = in.readString();
        this.comic_name = in.readString();
        this.comic_url = in.readString();
        this.chapter_name = in.readString();
        this.chapter_url = in.readString();
        this.comic_source = in.readString();
        this.cover = in.readString();
        this.status = in.readInt();
        this.position = in.readInt();
        this.total = in.readInt();
        this.create_time = in.readInt();
        this.next = in.readInt();
        this.prepare = in.readInt();
        this.next_url = in.readString();
        this.pre_url = in.readString();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel source) {
            return new DownloadInfo(source);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };
}
