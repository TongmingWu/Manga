package com.tongming.manga.mvp.api;

import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.MangaToken;
import com.tongming.manga.mvp.bean.Result;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.UserInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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
    @POST("/sms")
    Observable<Result> requestSms(@Body RequestBody body);

    //@Query("appId") String appId, @Query("appKey") String appKey, @Query("phone") String phone, @Query("code") String code
    /*@Headers("Content-Type:application/json")
    @POST("/verifySms")
    Observable<Result> verifySms(@Body RequestBody body);*/

    //@Query("phone") String phone, @Query("password") String password
    @Headers("Content-Type:application/json")
    @POST("/login")
    Observable<MangaToken> login(@Body RequestBody body);

    //@Query("phone") String phone, @Query("password") String password, @Query("name") String name
    //@Query("appId") String appId, @Query("appKey") String appKey, @Query("code") String code
    @Headers("Content-Type:application/json")
    @POST("/logon")
    Observable<Result> logon(@Body RequestBody body);

    @GET("/user")
    Observable<UserInfo> getUserInfo(@Query("token") String token);

    @POST("/user/update")
    Observable<UserInfo> updateUser(@Body RequestBody body);

    @Multipart
    @POST("/user/upload/{token}")
    Observable<UserInfo> uploadAvatar(@Part MultipartBody.Part file, @Path("token") String token);

    @Headers("Content-Type:application/json")
    @POST("/collection")
    Observable<UserInfo> addCollection(@Body RequestBody body);


    @Headers("Content-Type:application/json")
    @HTTP(method = "DELETE", path = "/collection", hasBody = true)
    Observable<UserInfo> deleteCollection(@Body RequestBody body);

    @GET("/collection")
    Observable<Result> queryCollection(@Query("uid") int uid, @Query("name") String name);
}
