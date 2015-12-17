package com.triskelapps.etherchusky;

/**
 * Created by julio on 16/12/15.
 */
public interface BaseView {

    void showProgress(String message);

    void hideProgress();

    void toast(int stringResId);

    void toast(String mensaje);

    void alert(String title, String message);

    void alert(String message);
}
