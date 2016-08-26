package com.tongming.manga.mvp.api;

import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.Sms;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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


    //@Query("appId") String appId, @Query("appKey") String appKey, @Query("phone") String phone
    @Headers("Content-Type:application/json")
    @POST("/requestSms")
    Observable<Sms> requestSms(@Body RequestBody body);

    //@Query("appId") String appId, @Query("appKey") String appKey, @Query("phone") String phone, @Query("code") String code
    @Headers("Content-Type:application/json")
    @POST("/verifySms")
    Observable<Sms> verifySms(@Body RequestBody body);

    //@Query("phone") String phone, @Query("password") String password
    @Headers("Content-Type:application/json")
    @POST("/login")
    Observable<User> login(@Body RequestBody body);

    //@Query("phone") String phone, @Query("password") String password, @Query("name") String name
    @Headers("Content-Type:application/json")
    @POST("/logon")
    Observable<User> logon(@Body RequestBody body);

    @GET("/user")
    Observable<UserInfo> getUserInfo(@Query("token") String token);

}
