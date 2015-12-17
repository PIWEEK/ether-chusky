package com.triskelapps.etherchusky.ui.main;

import android.content.Context;

import com.triskelapps.etherchusky.BasePresenter;

/**
 * Created by julio on 16/12/15.
 */
public class MainPresenter extends BasePresenter {


    private static MainPresenter sInstance;
    private final Context context;
    MainView view;


    public static MainPresenter getInstance(MainView view, Context context) {

        if (sInstance == null) {
            sInstance = new MainPresenter(view, context);

        }
        return sInstance;
    }

    private MainPresenter(MainView view, Context context) {

        this.view = view;
        this.context = context;


    }

    public void pruebaToast(String hoal) {

        baseView.toast("holaaaa");
    }
}
