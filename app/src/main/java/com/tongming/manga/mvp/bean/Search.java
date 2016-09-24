package com.tongming.manga.mvp.bean;

import java.util.List;

/**
 * Created by Tongming on 2016/8/7.
 */
public class Search {
    private int current_page;
    private int code;
    private String message;
    private boolean next;
    private String comic_id;
    /**
     * comic_url : /comic/13813/
     * newest_chapter_date : 2014-05-19
     * comic_author :  佚名
     * cover : http://tkres.tuku.cc/images/upload/20140506/13993585408031.jpg
     * comic_name : 狂乱果实
     * newest_chapter :
     */

    private List<ComicCard> result;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public List<ComicCard> getResult() {
        return result;
    }

    public void setResult(List<ComicCard> result) {
        this.result = result;
    }

    public void setComic_id(String comic_id) {
        this.comic_id = comic_id;
    }

    public String getComic_id() {

        return comic_id;
    }
}

