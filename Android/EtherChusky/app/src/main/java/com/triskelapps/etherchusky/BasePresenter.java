package com.triskelapps.etherchusky;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.triskelapps.etherchusky.util.Util;

/**
 * Created by julio on 16/12/15.
 */
public class BasePresenter {

    Context context;
    public BaseView baseView;

    protected SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    // -------- UTILS --------------
    protected boolean isConnected() {
        return Util.isConnected(context);
    }

    protected boolean isConnectedIfNotShowAlert() {

        boolean isConnected = isConnected();
        if (!isConnected) {
            baseView.alert(context.getString(R.string.no_connection));
        }

        return isConnected;
    }

    protected boolean isConnectedIfNotShowToast() {

        boolean isConnected = isConnected();
        if (!isConnected) {
            baseView.toast(context.getString(R.string.no_connection));
        }

        return isConnected;
    }

    public void setBaseView(BaseActivity baseView) {
        this.baseView = baseView;
    }
}
