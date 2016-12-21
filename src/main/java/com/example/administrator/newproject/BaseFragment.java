package com.example.administrator.newproject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.util.Log;

import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Administrator on 2016/12/4.
 */

public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getSimpleName();
    protected Subscription subscription;

    @OnClick(R.id.tipBt)
    void tip(){
        Log.i(TAG,"tipBt");
        new AlertDialog.Builder(getActivity())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogRes(),null))
                .show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
    }

    protected void unsubscribe(){
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
    protected abstract int getDialogRes();

    protected abstract int getTitleRes();
}
