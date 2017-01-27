package com.tongming.manga.mvp.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.tongming.manga.mvp.base.BaseApplication;
import com.tongming.manga.mvp.bean.Category;
import com.tongming.manga.mvp.bean.ComicInfo;
import com.tongming.manga.mvp.bean.ComicPage;
import com.tongming.manga.mvp.bean.Hot;
import com.tongming.manga.mvp.bean.MangaToken;
import com.tongming.manga.mvp.bean.Result;
import com.tongming.manga.mvp.bean.Search;
import com.tongming.manga.mvp.bean.User;
import com.tongming.manga.mvp.bean.UserInfo;
import com.tongming.manga.util.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Author: Tongming
 * Date: 2016/8/9
 */
public class ApiManager {
//            private static final String BASE_URL = "http://192.168.1.103:5000";
    private static final String BASE_URL = "http://119.29.57.187";

    public static final String APP_ID = "Rp2mOsDSVkDoN5E4hbJEhlig-gzGzoHsz";
    public static final String APP_KEY = "ri3VwT0ULLOqxtdjMPkhkgla";

    public static final String SOURCE_DMZJ = "dmzj";
    public static final String SOURCE_CC = "cc";
    public static final String SOURCE_IKAN = "ikan";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //短缓存有效期为120秒钟
    private static final int CACHE_STALE_SHORT = 120;
    //长缓存有效期为1天
    private static final int CACHE_STALE_LONG = 60 * 60 * 24;

    private OkHttpClient mOkHttpClient;

    private static ApiManager instance;
    private ApiService apiService;

    private static String source;
    private static SharedPreferences sp;

    private ApiManager() {
        initOkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        sp = BaseApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        source = sp.getString("source", SOURCE_DMZJ);
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager();
                }
            }
        }
        source = sp.getString("source", SOURCE_DMZJ);
        return instance;
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Context context = BaseApplication.getContext();
            if (!CommonUtil.isNet(context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (CommonUtil.isNet(context)) {
                //有网的时候读接口上的@Headers里的配置，可以在这里进行统一的设置
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + CACHE_STALE_SHORT)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (ApiManager.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(BaseApplication.getContext().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    public Observable<Hot> getHot() {
        return apiService.getHot(source);
    }

    public Observable<ComicInfo> getComicInfo(String source, String comicUrl) {
        return apiService.getComicInfo(source, comicUrl);
    }

    public Observable<ComicPage> getComicPage(String source, String chapterUrl) {
        return apiService.getComicPage(source, chapterUrl);
    }

    public Observable<Search> getComicType(int type, int page) {
        return apiService.getComicType(source, type, page);
    }

    public Observable<Category> getCategory() {
        return apiService.getCategory(source);
    }

    public Observable<Search> doSearch(String word, int page) {
        return apiService.doSearch(source, word, page);
    }

    public Observable<Result> requestSms(RequestBody body) {
        return apiService.requestSms(body);
    }

    public Observable<MangaToken> login(RequestBody body) {
        //登录成功得到token
        return apiService.login(body);
    }

    public Observable<Result> logon(RequestBody body) {
        return apiService.logon(body);
    }

    public Observable<UserInfo> getUserInfo(String token) {
        return apiService.getUserInfo(token);
    }

    public Observable<UserInfo> updateUser(RequestBody body) {
        return apiService.updateUser(body);
    }

    public Observable<UserInfo> uploadAvatar(MultipartBody.Part body) {
        return apiService.uploadAvatar(body, User.getInstance().getToken());
    }

    public Observable<UserInfo> addCollection(RequestBody body) {
        return apiService.addCollection(body);
    }

    public Observable<UserInfo> deleteCollection(RequestBody body) {
        return apiService.deleteCollection(body);
    }

    public Observable<Result> queryCollection(int uid, String name) {
        return apiService.queryCollection(uid, name);
    }

    public Observable<ResponseBody> downloadImage(String url, String source) {
        Observable<ResponseBody> observable = null;
        if (source.equals(SOURCE_DMZJ)) {
            observable = apiService.downloadImageByDMZJ(url);
        } else if (source.equals(SOURCE_IKAN)) {
            observable = apiService.downloadImageByIKAN(url);
        } else {
            observable = apiService.downloadImageByIKAN(url);
        }
        return observable;
    }

}
