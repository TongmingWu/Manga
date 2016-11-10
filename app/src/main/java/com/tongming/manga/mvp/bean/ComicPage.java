package com.tongming.manga.mvp.bean;

import java.util.List;

/**
 * Created by Tongming on 2016/8/9.
 */
public class ComicPage {
    /**
     * next : true
     * code : 200
     * img_list : ["http://tkpic.um5.cc/4700/20140924/c_176717/001.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/002.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/003.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/004.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/005.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/006.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/007.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/008.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/009.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/010.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/011.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/012.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/013.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/014.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/015.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/016.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/017.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/018.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/019.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/020.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/021.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/022.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/023.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/024.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/025.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/026.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/027.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/028.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/029.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/030.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/031.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/032.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/033.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/034.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/035.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/036.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/037.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/038.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/039.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/040.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/041.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/042.jpg","http://tkpic.um5.cc/4700/20140924/c_176717/043.jpg"]
     * page_count : 43
     * pre_chapter_url : /comic/14562/n-1410360438-91503/
     * next_chapter_url : /comic/14562/n-1412477539-62705/
     * chapter_name : 王室教师海涅第8话
     * prepare : true
     * message : 操作成功
     */

    private boolean next;
    private int code;
    private int page_count;
    private String pre_chapter_url;
    private String next_chapter_url;
    private String current_chapter_url;
    private String chapter_name;
    private String comic_source;
    private String comic_name;
    private boolean prepare;
    private String message;
    private List<String> img_list;

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public String getComic_name() {

        return comic_name;
    }

    public void setComic_source(String comic_source) {
        this.comic_source = comic_source;
    }

    public String getComic_source() {

        return comic_source;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public String getPre_chapter_url() {
        return pre_chapter_url;
    }

    public void setPre_chapter_url(String last_chapter_url) {
        this.pre_chapter_url = last_chapter_url;
    }

    public String getNext_chapter_url() {
        return next_chapter_url;
    }

    public void setNext_chapter_url(String next_chapter_url) {
        this.next_chapter_url = next_chapter_url;
    }

    public void setCurrent_chapter_url(String current_chapter_url) {
        this.current_chapter_url = current_chapter_url;
    }

    public String getCurrent_chapter_url() {

        return current_chapter_url;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public boolean isPrepare() {
        return prepare;
    }

    public void setPrepare(boolean prepare) {
        this.prepare = prepare;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getImg_list() {
        return img_list;
    }

    public void setImg_list(List<String> img_list) {
        this.img_list = img_list;
    }
}
