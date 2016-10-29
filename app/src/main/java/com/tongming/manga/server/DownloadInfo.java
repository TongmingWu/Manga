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

    public DownloadInfo(){

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

    protected DownloadInfo(Parcel in) {
        _id = in.readInt();
        comic_id = in.readString();
        comic_name = in.readString();
        comic_url = in.readString();
        chapter_name = in.readString();
        chapter_url = in.readString();
        comic_source = in.readString();
        cover = in.readString();
        status = in.readInt();
        position = in.readInt();
        total = in.readInt();
        create_time = in.readInt();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        @Override
        public DownloadInfo createFromParcel(Parcel in) {
            return new DownloadInfo(in);
        }

        @Override
        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };

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
        dest.writeInt(_id);
        dest.writeString(comic_id);
        dest.writeString(comic_name);
        dest.writeString(comic_url);
        dest.writeString(chapter_name);
        dest.writeString(chapter_url);
        dest.writeString(comic_source);
        dest.writeString(cover);
        dest.writeInt(status);
        dest.writeInt(position);
        dest.writeInt(total);
        dest.writeInt(create_time);
    }
}
