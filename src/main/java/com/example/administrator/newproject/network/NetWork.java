package com.example.administrator.newproject.network;

import com.example.administrator.newproject.network.api.FakeApi;
import com.example.administrator.newproject.network.api.GankApi;
import com.example.administrator.newproject.network.api.ZhuangbiApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/12/5.
 */

public class NetWork {
    private static FakeApi fakeApi;
    private static GankApi gankApi;
    private static ZhuangbiApi zhuangbiApi;
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static ZhuangbiApi getZhuangbiApi() {
        if (zhuangbiApi == null) {
            Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl("http://www.zhuangbi.info/")
                    .addConverterFactory(gsonConverterFactory).addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            zhuangbiApi = retrofit.create(ZhuangbiApi.class);
        }
        return zhuangbiApi;
    }

    public static GankApi gankApi(){
        if (gankApi == null){
            Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            gankApi = retrofit.create(GankApi.class);
        }
        return gankApi;
    }
    public static FakeApi getFakeApi(){
        if (fakeApi == null){
            fakeApi = new FakeApi();
        }
        return fakeApi;
    }

}
