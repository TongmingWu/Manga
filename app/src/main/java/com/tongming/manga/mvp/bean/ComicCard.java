package com.tongming.manga.mvp.bean;

/**
 * Created by Tongming on 2016/8/11.
 */
public class ComicCard {
    private String comic_url;
    private String newest_chapter_date;
    private String comic_author;
    private String cover;
    private String comic_name;
    private String newest_chapter;

    public String getComic_url() {
        return comic_url;
    }

    public void setComic_url(String comic_url) {
        this.comic_url = comic_url;
    }

    public String getNewest_chapter_date() {
        return newest_chapter_date;
    }

    public void setNewest_chapter_date(String newest_chapter_date) {
        this.newest_chapter_date = newest_chapter_date;
    }

    public String getComic_author() {
        return comic_author;
    }

    public void setComic_author(String comic_author) {
        this.comic_author = comic_author;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getComic_name() {
        return comic_name;
    }

    public void setComic_name(String comic_name) {
        this.comic_name = comic_name;
    }

    public String getNewest_chapter() {
        return newest_chapter;
    }

    public void setNewest_chapter(String newest_chapter) {
        this.newest_chapter = newest_chapter;
    }
}
