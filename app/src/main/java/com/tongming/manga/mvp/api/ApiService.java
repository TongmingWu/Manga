package com.tongming.manga.mvp.api;

import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.Search;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Tongming on 2016/8/9.
 */
public interface ApiService {
    @GET("/cc")
    Observable<Hot> getHot();

    @GET("/cc/comic/detail")
    Observable<ComicInfo> getComicInfo(@Query("comic_url") String comicUrl);

    @GET("/cc/comic/view")
    Observable<ComicPage> getComicPage(@Query("chapter_url") String chapterUrl);

    @GET("/cc/comic/category")
    Observable<Search> getComicType(@Query("select") int select, @Query("type") int type, @Query("page") int page);

    @GET("/cc/search")
    Observable<Search> doSearch(@Query("word") String word, @Query("page") int page);


}
