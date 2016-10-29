package com.tongming.manga.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Tongming on 2016/8/9.
 */
public class ComicInfo implements Parcelable {

    /**
     * cover : http://images.dmzj.com/webpic/8/160131shuangxing2fml.jpg
     * status : 连载中
     * comic_type : 冒险 神鬼
     * newest_chapter_url : http://m.dmzj.com/view/13318/53554.html
     * desc : 介绍:要守护的就要振奋起来！我要用的手和我的剑守护这个世界！
     * comic_area : 日本
     * newest_chapter : 第34话
     * message : 操作成功
     * comic_author : 助野嘉昭
     * comic_id : sxzyys
     * code : 200
     * newest_chapter_date : 2016-09-20 04:09
     * similar_comic_list : []
     * chapter_list : [{"chapter_title":"第34话","chapter_url":"http://m.dmzj.com/view/13318/53554.html"},{"chapter_title":"番外篇7","chapter_url":"http://m.dmzj.com/view/13318/52971.html"},{"chapter_title":"第33话","chapter_url":"http://m.dmzj.com/view/13318/52844.html"},{"chapter_title":"第32话","chapter_url":"http://m.dmzj.com/view/13318/51243.html"},{"chapter_title":"第31话","chapter_url":"http://m.dmzj.com/view/13318/49978.html"},{"chapter_title":"第30话","chapter_url":"http://m.dmzj.com/view/13318/49362.html"},{"chapter_title":"特别篇","chapter_url":"http://m.dmzj.com/view/13318/48856.html"},{"chapter_title":"第29话","chapter_url":"http://m.dmzj.com/view/13318/48081.html"},{"chapter_title":"第28话","chapter_url":"http://m.dmzj.com/view/13318/46668.html"},{"chapter_title":"第27话","chapter_url":"http://m.dmzj.com/view/13318/45352.html"},{"chapter_title":"第26话","chapter_url":"http://m.dmzj.com/view/13318/44945.html"},{"chapter_title":"第25话","chapter_url":"http://m.dmzj.com/view/13318/43891.html"},{"chapter_title":"第24话","chapter_url":"http://m.dmzj.com/view/13318/43691.html"},{"chapter_title":"第23话","chapter_url":"http://m.dmzj.com/view/13318/42191.html"},{"chapter_title":"第22话","chapter_url":"http://m.dmzj.com/view/13318/41261.html"},{"chapter_title":"番外篇6","chapter_url":"http://m.dmzj.com/view/13318/39830.html"},{"chapter_title":"第21话","chapter_url":"http://m.dmzj.com/view/13318/39829.html"},{"chapter_title":"番外篇5","chapter_url":"http://m.dmzj.com/view/13318/38870.html"},{"chapter_title":"第20话","chapter_url":"http://m.dmzj.com/view/13318/38869.html"},{"chapter_title":"第19话","chapter_url":"http://m.dmzj.com/view/13318/37588.html"},{"chapter_title":"第18话","chapter_url":"http://m.dmzj.com/view/13318/36868.html"},{"chapter_title":"第17话","chapter_url":"http://m.dmzj.com/view/13318/36153.html"},{"chapter_title":"番外篇4","chapter_url":"http://m.dmzj.com/view/13318/35804.html"},{"chapter_title":"第16话","chapter_url":"http://m.dmzj.com/view/13318/34376.html"},{"chapter_title":"第15话","chapter_url":"http://m.dmzj.com/view/13318/33669.html"},{"chapter_title":"第14话","chapter_url":"http://m.dmzj.com/view/13318/32975.html"},{"chapter_title":"第13话","chapter_url":"http://m.dmzj.com/view/13318/32296.html"},{"chapter_title":"番外篇3","chapter_url":"http://m.dmzj.com/view/13318/32295.html"},{"chapter_title":"第12话","chapter_url":"http://m.dmzj.com/view/13318/31462.html"},{"chapter_title":"第11话","chapter_url":"http://m.dmzj.com/view/13318/30873.html"},{"chapter_title":"第10话","chapter_url":"http://m.dmzj.com/view/13318/29652.html"},{"chapter_title":"第09话","chapter_url":"http://m.dmzj.com/view/13318/29186.html"},{"chapter_title":"番外篇2","chapter_url":"http://m.dmzj.com/view/13318/28871.html"},{"chapter_title":"第08话","chapter_url":"http://m.dmzj.com/view/13318/28597.html"},{"chapter_title":"第07话","chapter_url":"http://m.dmzj.com/view/13318/28097.html"},{"chapter_title":"第06话","chapter_url":"http://m.dmzj.com/view/13318/27628.html"},{"chapter_title":"第05话","chapter_url":"http://m.dmzj.com/view/13318/27207.html"},{"chapter_title":"第04话","chapter_url":"http://m.dmzj.com/view/13318/26658.html"},{"chapter_title":"第03话","chapter_url":"http://m.dmzj.com/view/13318/26127.html"},{"chapter_title":"番外篇","chapter_url":"http://m.dmzj.com/view/13318/25853.html"},{"chapter_title":"第02话","chapter_url":"http://m.dmzj.com/view/13318/25557.html"},{"chapter_title":"第01话","chapter_url":"http://m.dmzj.com/view/13318/25213.html"},{"chapter_title":"第05卷","chapter_url":"http://m.dmzj.com/view/13318/46765.html"},{"chapter_title":"第04卷","chapter_url":"http://m.dmzj.com/view/13318/48103.html"},{"chapter_title":"第03卷","chapter_url":"http://m.dmzj.com/view/13318/48102.html"},{"chapter_title":"第02卷","chapter_url":"http://m.dmzj.com/view/13318/48101.html"},{"chapter_title":"第01卷","chapter_url":"http://m.dmzj.com/view/13318/48100.html"}]
     * comic_url : http://m.dmzj.com/info/sxzyys.html
     * comic_source : dmzj
     * comic_name : 双星之阴阳师
     */

    private String cover;
    private String status;
    private String comic_type;
    private String newest_chapter_url;
    private String desc;
    private String comic_area;
    private String newest_chapter;
    private String message;
    private String comic_author;
    private String comic_id;
    private int code;
    private String newest_chapter_date;
    private String comic_url;
    private String comic_source;
    private String comic_name;
    private List<?> similar_comic_list;
    /**
     * chapter_title : 第34话
     * chapter_url : http://m.dmzj.com/view/13318/53554.html
     */

    private List<ChapterListBean> chapter_list;

    protected ComicInfo(Parcel in) {
        cover = in.readString();
        status = in.readString();
        comic_type = in.readString();
        newest_chapter_url = in.readString();
        desc = in.readString();
        comic_area = in.readString();
        newest_chapter = in.readString();
        message = in.readString();
        comic_author = in.readString();
        comic_id = in.readString();
        code = in.readInt();
        newest_chapter_date = in.readString();
        comic_url = in.readString();
        comic_source = in.readString();
        comic_name = in.readString();
        chapter_list = in.createTypedArrayList(ChapterListBean.CREATOR);
    }

    public static final Creator<ComicInfo> CREATOR = new Creator<ComicInfo>() {
        @Override
        public ComicInfo createFromParcel(Parcel in) {
            return new ComicInfo(in);
        }

        @Override
        public ComicInfo[] newArray(int size) {
            return new ComicInfo[size];
        }
    };

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComic_type() {
        return comic_type;
    }

    public void setComic_type(String comic_type) {
        this.comic_type = comic_type;
    }

    public String getNewest_chapter_url() {
        return newest_chapter_url;
    }

    public void setNewest_chapter_url(String newest_chapter_url) {
        this.newest_chapter_url = newest_chapter_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getComic_area() {
        return comic_area;
    }

    public void setComic_area(String comic_area) {
        this.comic_area = comic_area;
    }

    public String getNewest_chapter() {
        return newest_chapter;
    }

    public void setNewest_chapter(String newest_chapter) {
        this.newest_chapter = newest_chapter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getComic_author() {
        return comic_author;
    }

    public void setComic_author(String comic_author) {
        this.comic_author = comic_author;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNewest_chapter_date() {
        return newest_chapter_date;
    }

    public void setNewest_chapter_date(String newest_chapter_date) {
        this.newest_chapter_date = newest_chapter_date;
    }

    public String getComic_url() {
        return comic_url;
    }

    public void setComic_url(String comic_url) {
        this.comic_url = comic_url;
    }

    public String getComic_source() {
        return comic_source;
    }

    public void setComic_source(String comic_source) {
        this.comic_source = comic_source;
    }

    public String getComic_name() {
        return comic_name;
    }

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public List<?> getSimilar_comic_list() {
        return similar_comic_list;
    }

    public void setSimilar_comic_list(List<?> similar_comic_list) {
        this.similar_comic_list = similar_comic_list;
    }

    public List<ChapterListBean> getChapter_list() {
        return chapter_list;
    }

    public void setChapter_list(List<ChapterListBean> chapter_list) {
        this.chapter_list = chapter_list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cover);
        dest.writeString(status);
        dest.writeString(comic_type);
        dest.writeString(newest_chapter_url);
        dest.writeString(desc);
        dest.writeString(comic_area);
        dest.writeString(newest_chapter);
        dest.writeString(message);
        dest.writeString(comic_author);
        dest.writeString(comic_id);
        dest.writeInt(code);
        dest.writeString(newest_chapter_date);
        dest.writeString(comic_url);
        dest.writeString(comic_source);
        dest.writeString(comic_name);
        dest.writeTypedList(chapter_list);
    }

    public static class ChapterListBean implements Parcelable {
        private String chapter_title;
        private String chapter_url;

        public String getChapter_title() {
            return chapter_title;
        }

        public void setChapter_title(String chapter_title) {
            this.chapter_title = chapter_title;
        }

        public String getChapter_url() {
            return chapter_url;
        }

        public void setChapter_url(String chapter_url) {
            this.chapter_url = chapter_url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.chapter_title);
            dest.writeString(this.chapter_url);
        }

        public ChapterListBean() {
        }

        protected ChapterListBean(Parcel in) {
            this.chapter_title = in.readString();
            this.chapter_url = in.readString();
        }

        public static final Creator<ChapterListBean> CREATOR = new Creator<ChapterListBean>() {
            @Override
            public ChapterListBean createFromParcel(Parcel source) {
                return new ChapterListBean(source);
            }

            @Override
            public ChapterListBean[] newArray(int size) {
                return new ChapterListBean[size];
            }
        };
    }
}
