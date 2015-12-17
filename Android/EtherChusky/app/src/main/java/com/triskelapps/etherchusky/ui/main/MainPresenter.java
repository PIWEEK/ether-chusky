package com.triskelapps.etherchusky.ui.main;

import android.content.Context;

import com.triskelapps.etherchusky.App;
import com.triskelapps.etherchusky.BasePresenter;
import com.triskelapps.etherchusky.R;
import com.triskelapps.etherchusky.api.AccountApi;
import com.triskelapps.etherchusky.api.ApiClient;
import com.triskelapps.etherchusky.model.Account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 16/12/15.
 */
public class MainPresenter extends BasePresenter implements Serializable {


    private static MainPresenter sInstance;
    List<MainView> views = new ArrayList<>();


    public static MainPresenter getInstance(MainView view, Context context) {

        if (sInstance == null) {
            sInstance = new MainPresenter(view, context);

        }
        return sInstance;
    }

    private MainPresenter(MainView view, Context context) {

        this.views.add(view);
        this.context = context;


    }

    protected void onResume() {

        String username = getPrefs().getString(App.SHARED_USERNAME, "---");
        for (MainView view : views) {

            view.setUsername(username);
        }
    }


    private AccountApi getApi() {
        return ApiClient.getInstance(context).create(AccountApi.class);
    }


    public void getAccounts() {

        baseView.showProgress(context.getString(R.string.loading));

        getApi().getAccounts()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Account>>() {
                    @Override
                    public void onCompleted() {

                        baseView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {

                        baseView.hideProgress();
                        baseView.toast(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Account> accounts) {

                        for (MainView view : views) {
                            view.listAccounts(accounts);
                        }
                    }
                });


    }

    public void addView(MainView view) {
        views.add(view);
    }

    public void removeView(MainView view) {
        views.remove(view);
    }

    public void sendTransaction(float amount, String destination) {
        baseView.toast("amount: " + amount + ", destination: " + destination);
    }
}
