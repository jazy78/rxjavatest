package com.example.hp.rxjavatext.LoadMangager;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.hp.rxjavatext.model.GoodsList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by hp on 2016/3/30.
 */
public class LoadManager {

    public static LoadManager instance;
    private Context mcontext;

    public static LoadManager getInstance(Context context) {

        if (instance == null) {

            synchronized (LoadManager.class) {

                instance = new LoadManager(context);
            }

        }

        return instance;
    }

    public static final String GANK_SERVER_IP = "http://gank.avosapps.com/api";

    public static Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    public final NetWorkRequest netWorkRequest;

    public LoadManager(Context context) {

        //初始化Okhttp
        Cache cache;
        OkHttpClient okHttpClient = null;
        try {

            File cacheDir = new File(context.getCacheDir().getPath(), "gank_cache.json");
            cache = new Cache(cacheDir, 10 * 1024 * 1024);
            okHttpClient = new OkHttpClient();
            okHttpClient.setCache(cache);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //初始化Retrofit ,Retrofit会使用OkHttp来进行网络请求，

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(GANK_SERVER_IP) //设置初始URL连接地址
                .setClient(new OkClient(okHttpClient))//内部用okhttp进行网络请求
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(interceptor)
                .build();
        netWorkRequest = restAdapter.create(NetWorkRequest.class);
        //实现NetWorkRequest接口


    }

    private RequestInterceptor interceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
            request.addHeader("Cache-Control", "public, max-age=" + 60 * 60 * 4);
            request.addHeader("Content-Type", "application/json");
        }
    };


    public interface NetWorkRequest {

        @GET("/data/Android/{limit}/{page}")
        Observable<GoodsList> getAndroidGoodList(
                @Path("limit") int limit, @Path("page") int page
          );


        @GET("/data/iOS/{limit}/{page}")
        Observable<GoodsList> getiOSGoodList(
                @Path("limit")int limit,
                @Path("page")int page
        );

    }

    public Observable<GoodsList> getGoodLists(String type,int limit,int page ){

       if(type.equalsIgnoreCase("Android")){
           Log.d("AAA", type + "=我到Android了");
           return netWorkRequest.getAndroidGoodList(limit,page);
       }else {
           Log.d("AAA",type+"我到IOS了");
           return netWorkRequest.getiOSGoodList(limit,page);
       }

   }


}
