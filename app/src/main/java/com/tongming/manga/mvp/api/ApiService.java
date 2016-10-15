package com.tongming.manga.mvp.api;

import com.tongming.manga.mvp.bean.Category;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.MangaToken;
import com.tongming.manga.mvp.bean.Result;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.UserInfo;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Author: Tongming
 * Date: 2016/8/9
 */
interface ApiService {
    //TODO 资源整合
    /*@GET("/cc")
    Observable<Hot> getHot();*/

    /*@GET("/dmzj")
    Observable<Hot> getHot();*/

    @GET("/")
    Observable<Hot> getHot(@Query("source") String source);

    /*@GET("/cc/comic/detail")
    Observable<ComicInfo> getComicInfo(@Query("comic_url") String comicUrl);*/

    /*@GET("/dmzj/comic/detail")
    Observable<ComicInfo> getComicInfo(@Query("comic_url") String comicUrl);*/

    @GET("/comic/detail")
    Observable<ComicInfo> getComicInfo(@Query("source") String source, @Query("comic_url") String comicUrl);

    /*@GET("/cc/comic/view")
    Observable<ComicPage> getComicPage(@Query("chapter_url") String chapterUrl);*/

    /*@GET("/dmzj/comic/view")
    Observable<ComicPage> getComicPage(@Query("chapter_url") String chapterUrl);*/

    @GET("/comic/view")
    Observable<ComicPage> getComicPage(@Query("source") String source, @Query("chapter_url") String chapterUrl);

    /*@GET("/cc/comic/category")
    Observable<Search> getComicType(@Query("type") int type, @Query("page") int page);*/

    /*@GET("/dmzj/comic/category")
    Observable<Search> getComicType(@Query("type") int type, @Query("page") int page);*/

    @GET("/comic/category")
    Observable<Search> getComicType(@Query("source") String source, @Query("type") int type, @Query("page") int page);

    /*@GET("/cc/comic/category")
    Observable<Category> getCategory();*/

    /*@GET("/dmzj/comic/category")
    Observable<Category> getCategory();*/

    @GET("/comic/category")
    Observable<Category> getCategory(@Query("source") String source);

    /*@GET("/cc/search")
    Observable<Search> doSearch(@Query("word") String word, @Query("page") int page);*/

    /*@GET("/dmzj/search")
    Observable<Search> doSearch(@Query("word") String word, @Query("page") int page);*/

    @GET("/search")
    Observable<Search> doSearch(@Query("source") String source, @Query("word") String word, @Query("page") int page);


    @Headers("Content-Type:application/json")
    @POST("/sms")
    Observable<Result> requestSms(@Body RequestBody body);

    @Headers("Content-Type:application/json")
    @POST("/login")
    Observable<MangaToken> login(@Body RequestBody body);

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

    @Headers("Referer:http://m.dmzj.com/")
    @GET
    Observable<ResponseBody> downloadImage(@Url String url);
}
