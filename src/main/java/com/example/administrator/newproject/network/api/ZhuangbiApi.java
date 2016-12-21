package com.example.administrator.newproject.network.api;

import com.example.administrator.newproject.model.ZhuangbiImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/12/5.
 */

public interface ZhuangbiApi {
    @GET("search")
    rx.Observable<List<ZhuangbiImage>> search(@Query("q")String query);
}

