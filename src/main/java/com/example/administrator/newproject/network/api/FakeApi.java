package com.example.administrator.newproject.network.api;

import com.example.administrator.newproject.model.FakeThing;
import com.example.administrator.newproject.model.FakeToken;

import java.util.Random;

import rx.Observable;
import rx.functions.Func1;

/**
 * token标记，正常使用时将耗时操作换做成网络请求方式和ZhuangbiApi、GankApi中的方法一样
 * token获取的标记expired（过期）则在第二个方法中得不到数据。
 * Created by Administrator on 2016/12/20.
 */

public class FakeApi {
    Random random = new Random();

    public Observable<FakeToken> getFakeToken(String fakeAuth) {
        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() {
                    @Override
                    public FakeToken call(String s) {
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;//相当与Math.random()*500+500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FakeToken fakeToken = new FakeToken();
                        fakeToken.token = createToken();
                        return fakeToken;
                    }
                });

    }

    private static String createToken() {
        return "fake_token_" + System.currentTimeMillis() % 10000;
    }
    public Observable<FakeThing> getFakeData(FakeToken fakeToken){
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, FakeThing>() {
                    @Override
                    public FakeThing call(FakeToken fakeToken) {
                        int fakeNetworkTimeCost = random.nextInt(500)+500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (fakeToken.expired){//如果已经过期
                            throw new IllegalArgumentException("Token expired!");
                        }
                        FakeThing fakeThing = new FakeThing();
                        fakeThing.id = (int) (System.currentTimeMillis()%1000);
                        fakeThing.name =  "FAKE_USER_" + fakeThing.id;
                        return fakeThing;
                    }
                });
    }
}
