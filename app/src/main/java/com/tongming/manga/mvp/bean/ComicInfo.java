package com.tongming.manga.mvp.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tongming on 2016/8/9.
 */
public class ComicInfo implements Parcelable {
    /**
     * chapter_list : [{"chapter_url":"/comic/13650/n-1474014902-81749/","chapter_title":"第53话"},{"chapter_url":"/comic/13650/n-1473761581-90413/","chapter_title":"第52话"},{"chapter_url":"/comic/13650/n-1473056041-49926/","chapter_title":"第51话"},{"chapter_url":"/comic/13650/n-1473056041-33261/","chapter_title":"第50话"},{"chapter_url":"/comic/13650/n-1471342982-18403/","chapter_title":"第49话"},{"chapter_url":"/comic/13650/n-1471342982-66511/","chapter_title":"第48话"},{"chapter_url":"/comic/13650/n-1471342982-90066/","chapter_title":"第47话"},{"chapter_url":"/comic/13650/n-1471342982-26686/","chapter_title":"第46话"},{"chapter_url":"/comic/13650/n-1467698162-90848/","chapter_title":"第45话"},{"chapter_url":"/comic/13650/n-1467373382-54353/","chapter_title":"第44话"},{"chapter_url":"/comic/13650/n-1467373382-78730/","chapter_title":"第43话"},{"chapter_url":"/comic/13650/n-1466430602-55313/","chapter_title":"第42话"},{"chapter_url":"/comic/13650/n-1465295642-82479/","chapter_title":"第39话"},{"chapter_url":"/comic/13650/n-1471342982-84453/","chapter_title":"第38话"},{"chapter_url":"/comic/13650/n-1460396223-23787/","chapter_title":"第37话"},{"chapter_url":"/comic/13650/n-1459156981-30613/","chapter_title":"第36话"},{"chapter_url":"/comic/13650/n-1458632282-99277/","chapter_title":"第35话"},{"chapter_url":"/comic/13650/n-1458311882-60149/","chapter_title":"第34话"},{"chapter_url":"/comic/13650/n-1458057301-98940/","chapter_title":"第33话"},{"chapter_url":"/comic/13650/n-1457245802-96910/","chapter_title":"第32话"},{"chapter_url":"/comic/13650/n-1456571402-76951/","chapter_title":"第31话"},{"chapter_url":"/comic/13650/n-1458057301-10510/","chapter_title":"第30话"},{"chapter_url":"/comic/13650/n-1458057361-77788/","chapter_title":"第29话"},{"chapter_url":"/comic/13650/n-1433644161-14213/","chapter_title":"第28话"},{"chapter_url":"/comic/13650/n-1431758424-68007/","chapter_title":"第27话"},{"chapter_url":"/comic/13650/n-1431142004-50446/","chapter_title":"第26话"},{"chapter_url":"/comic/13650/n-1429173902-42930/","chapter_title":"第25话"},{"chapter_url":"/comic/13650/n-1428548709-55509/","chapter_title":"第24话"},{"chapter_url":"/comic/13650/n-1426387602-28281/","chapter_title":"第23话"},{"chapter_url":"/comic/13650/n-1425522509-36778/","chapter_title":"第22话"},{"chapter_url":"/comic/13650/n-1423634710-68462/","chapter_title":"第21话"},{"chapter_url":"/comic/13650/n-1422934520-34262/","chapter_title":"第20话"},{"chapter_url":"/comic/13650/n-1421291063-44433/","chapter_title":"第19话"},{"chapter_url":"/comic/13650/n-1420597088-96269/","chapter_title":"第18话"},{"chapter_url":"/comic/13650/n-1418454460-71639/","chapter_title":"第17话"},{"chapter_url":"/comic/13650/n-1417484761-22814/","chapter_title":"第16话"},{"chapter_url":"/comic/13650/n-1415627198-92370/","chapter_title":"第15话"},{"chapter_url":"/comic/13650/n-1414984529-51340/","chapter_title":"第14话"},{"chapter_url":"/comic/13650/n-1413111307-42621/","chapter_title":"第13话"},{"chapter_url":"/comic/13650/n-1412473364-96390/","chapter_title":"第12话"},{"chapter_url":"/comic/13650/n-1411916333-37383/","chapter_title":"第11话"},{"chapter_url":"/comic/13650/n-1408334710-92546/","chapter_title":"第10话"},{"chapter_url":"/comic/13650/n-1407575714-94981/","chapter_title":"第9话"},{"chapter_url":"/comic/13650/n-1405228625-58352/","chapter_title":"第8话"},{"chapter_url":"/comic/13650/n-1404873351-90675/","chapter_title":"第7话"},{"chapter_url":"/comic/13650/n-1403059916-83512/","chapter_title":"第6话"},{"chapter_url":"/comic/13650/n-1402371332-34341/","chapter_title":"第5话"},{"chapter_url":"/comic/13650/n-1400636062-50126/","chapter_title":"第4话"},{"chapter_url":"/comic/13650/n-1399538384-13228/","chapter_title":"第3话"},{"chapter_url":"/comic/13650/n-1398930748-10749/","chapter_title":"第2话"},{"chapter_url":"/comic/13650/n-1397357229-64786/","chapter_title":"第1话"}]
     * code : 200
     * comic_url : /comic/13650/
     * comic_id : 13650
     * newest_chapter : 第53话
     * newest_chapter_date : 2016-09-16 16:35:02
     * comic_name : GTO失乐园
     * download_url : /comic/down.htm#13650
     * comic_area : 日本
     * desc :  GTO失乐园漫画 ，倍日本青少年所崇爱，被家长团所厌恶的传说中的男人回来了！等候多时的GTO失乐园终于开始连载，舞台当然是学院……不是？鬼塚英吉，麻辣？教师？鬼塚？厲兵秣马的男人以意想不到的样子登场。他究竟是为何被关押？被关押的理由是诱拐学生？究竟是怎么回事？敬请欣赏。
     * status : 连载中
     * cover : http://tkres.tuku.cc/images/upload/20140413/13973570514242.jpg
     * comic_author : 藤泽亨
     * comic_type : 少年热血 轻松搞笑
     * newest_chapter_url : /comic/13650/n-1474014902-81749/
     * message : 操作成功
     */

    private int code;
    private String comic_url;
    private String comic_id;
    private String newest_chapter;
    private String newest_chapter_date;
    private String comic_name;
    private String download_url;
    private String comic_source;
    private String comic_area;
    private String desc;
    private String status;
    private String cover;
    private String comic_author;
    private String comic_type;
    private String newest_chapter_url;
    private String message;
    /**
     * chapter_url : /comic/13650/n-1474014902-81749/
     * chapter_title : 第53话
     */

    private List<ChapterListBean> chapter_list;
    private List<ComicCard> similar_list;

    public void setSimilar_list(List<ComicCard> similar_list) {
        this.similar_list = similar_list;
    }

    public List<ComicCard> getSimilar_list() {

        return similar_list;
    }

    public void setComic_source(String comic_source) {
        this.comic_source = comic_source;
    }

    public String getComic_source() {

        return comic_source;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getComic_url() {
        return comic_url;
    }

    public void setComic_url(String comic_url) {
        this.comic_url = comic_url;
    }

    public String getComic_id() {
        return comic_id;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getNewest_chapter() {
        return newest_chapter;
    }

    public void setNewest_chapter(String newest_chapter) {
        this.newest_chapter = newest_chapter;
    }

    public String getNewest_chapter_date() {
        return newest_chapter_date;
    }

    public void setNewest_chapter_date(String newest_chapter_date) {
        this.newest_chapter_date = newest_chapter_date;
    }

    public String getComic_name() {
        return comic_name;
    }

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    public String getComic_area() {
        return comic_area;
    }

    public void setComic_area(String comic_area) {
        this.comic_area = comic_area;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getComic_author() {
        return comic_author;
    }

    public void setComic_author(String comic_author) {
        this.comic_author = comic_author;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ChapterListBean> getChapter_list() {
        return chapter_list;
    }

    public void setChapter_list(List<ChapterListBean> chapter_list) {
        this.chapter_list = chapter_list;
    }

    public static class ChapterListBean implements Parcelable {
        private String chapter_url;
        private String chapter_title;

        public String getChapter_url() {
            return chapter_url;
        }

        public void setChapter_url(String chapter_url) {
            this.chapter_url = chapter_url;
        }

        public String getChapter_title() {
            return chapter_title;
        }

        public void setChapter_title(String chapter_title) {
            this.chapter_title = chapter_title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.chapter_url);
            dest.writeString(this.chapter_title);
        }

        public ChapterListBean() {
        }

        protected ChapterListBean(Parcel in) {
            this.chapter_url = in.readString();
            this.chapter_title = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.comic_url);
        dest.writeString(this.comic_id);
        dest.writeString(this.newest_chapter);
        dest.writeString(this.newest_chapter_date);
        dest.writeString(this.comic_name);
        dest.writeString(this.download_url);
        dest.writeString(this.comic_area);
        dest.writeString(this.desc);
        dest.writeString(this.status);
        dest.writeString(this.cover);
        dest.writeString(this.comic_author);
        dest.writeString(this.comic_type);
        dest.writeString(this.newest_chapter_url);
        dest.writeString(this.message);
        dest.writeList(this.chapter_list);
    }

    public ComicInfo() {
    }

    protected ComicInfo(Parcel in) {
        this.code = in.readInt();
        this.comic_url = in.readString();
        this.comic_id = in.readString();
        this.newest_chapter = in.readString();
        this.newest_chapter_date = in.readString();
        this.comic_name = in.readString();
        this.download_url = in.readString();
        this.comic_area = in.readString();
        this.desc = in.readString();
        this.status = in.readString();
        this.cover = in.readString();
        this.comic_author = in.readString();
        this.comic_type = in.readString();
        this.newest_chapter_url = in.readString();
        this.message = in.readString();
        this.chapter_list = new ArrayList<ChapterListBean>();
        in.readList(this.chapter_list, ChapterListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ComicInfo> CREATOR = new Parcelable.Creator<ComicInfo>() {
        @Override
        public ComicInfo createFromParcel(Parcel source) {
            return new ComicInfo(source);
        }

        @Override
        public ComicInfo[] newArray(int size) {
            return new ComicInfo[size];
        }
    };
}
