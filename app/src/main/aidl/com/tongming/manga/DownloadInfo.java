package com.tongming.manga;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Tongming
 * Date: 2016/9/7
 */

public class DownloadInfo implements Parcelable {
    private int _id;
    private int comic_id;
    private String comic_name;
    private String comic_url;
    private String chapter_name;
    private String chapter_url;
    private String cover;
    private String comic_source;
    private int status;
    private int position;
    private int total;
    private int create_time;

    public DownloadInfo(){}

    protected DownloadInfo(Parcel in) {
        _id = in.readInt();
        comic_id = in.readInt();
        comic_name = in.readString();
        comic_url = in.readString();
        chapter_name = in.readString();
        chapter_url = in.readString();
        cover = in.readString();
        comic_source = in.readString();
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

    public int get_id() {
        return _id;
    }

    public int getComic_id() {
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

    public String getCover() {
        return cover;
    }

    public String getComic_source() {
        return comic_source;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeInt(comic_id);
        dest.writeString(comic_name);
        dest.writeString(comic_url);
        dest.writeString(chapter_name);
        dest.writeString(chapter_url);
        dest.writeString(cover);
        dest.writeString(comic_source);
        dest.writeInt(status);
        dest.writeInt(position);
        dest.writeInt(total);
        dest.writeInt(create_time);
    }
}
