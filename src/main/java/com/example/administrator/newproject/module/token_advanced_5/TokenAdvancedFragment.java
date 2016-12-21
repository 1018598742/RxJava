package com.example.administrator.newproject.module.token_advanced_5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.newproject.BaseFragment;
import com.example.administrator.newproject.R;
import com.example.administrator.newproject.model.FakeThing;
import com.example.administrator.newproject.model.FakeToken;
import com.example.administrator.newproject.network.NetWork;
import com.example.administrator.newproject.network.api.FakeApi;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 高级标记碎片
 * Created by Administrator on 2016/12/20.
 */

public class TokenAdvancedFragment extends BaseFragment {
    @Bind(R.id.tokenAdvancedTv)
    TextView tokenAdTv;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    boolean tokenUpdate;
    FakeToken cacheFakeToken = new FakeToken(true);

    @OnClick(R.id.invalidateBt)
    void invalidate() {
        cacheFakeToken.expired = true;
        Toast.makeText(getActivity(), R.string.token_destroyed, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.requestAdBt)
    void upload() {
        tokenUpdate = false;
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final FakeApi fakeApi = NetWork.getFakeApi();
        subscription = Observable.just(null)
                .flatMap(new Func1<Object, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> call(Object o) {
                        return cacheFakeToken.token == null
                                ? Observable.<FakeThing>error(new NullPointerException("Token is null!"))
                                : fakeApi.getFakeData(cacheFakeToken);
                    }
                })
                .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                    @Override
                    public Observable<?> call(final Observable<? extends Throwable> observable) {

                        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                            @Override
                            public Observable<?> call(Throwable throwable) {
                                if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
                                    return fakeApi.getFakeToken("fake_auth_code")
                                            .doOnNext(new Action1<FakeToken>() {
                                                @Override
                                                public void call(FakeToken fakeToken) {
                                                    tokenUpdate = true;
                                                    cacheFakeToken.token = fakeToken.token;
                                                    cacheFakeToken.expired = fakeToken.expired;
                                                }
                                            });
                                }
                                return Observable.error(throwable);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<FakeThing>() {
                    @Override
                    public void call(FakeThing fakeThing) {
                        swipeRefreshLayout.setRefreshing(false);
                        String token = cacheFakeToken.token;
                        if (tokenUpdate) {
                            token += "(" + getString(R.string.updated) + ")";
                        }
//                        tokenAdTv.setText(getString(R.string.got_token_and_data),token,fakeThing.id,fakeThing.name);
                        tokenAdTv.setText(getString(R.string.got_token_and_data, token, fakeThing.id, fakeThing.name));
                    }

                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), getString(R.string.loading_failed), Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token_advanced, container, false);
//        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);

        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token_advanced;
    }
}
