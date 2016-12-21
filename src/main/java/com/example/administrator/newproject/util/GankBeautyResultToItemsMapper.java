package com.example.administrator.newproject.util;

import com.example.administrator.newproject.model.GankBeauty;
import com.example.administrator.newproject.model.GankBeautyResult;
import com.example.administrator.newproject.model.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Administrator on 2016/12/18.
 */

public class GankBeautyResultToItemsMapper implements Func1<GankBeautyResult,List<Item>> {
    private static GankBeautyResultToItemsMapper INSTANCE = new GankBeautyResultToItemsMapper();
    private GankBeautyResultToItemsMapper(){

    }
    public static GankBeautyResultToItemsMapper getInstance(){
        return INSTANCE;
    }
    @Override
    public List<Item> call(GankBeautyResult gankBeautyResult) {
        List<GankBeauty>  gankBeauties = gankBeautyResult.beauties;
        List<Item> items = new ArrayList<>(gankBeauties.size());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for (GankBeauty gankBeauty:  gankBeauties){
            Item item = new Item();
            item.setImageUrl(gankBeauty.getUrl());
            try {
                Date date = inputFormat.parse(gankBeauty.getCreatedAt());
                item.setDescription(outputFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
                item.setDescription("unknown date");
            }
            items.add(item);
        }
        return items;
    }
}
