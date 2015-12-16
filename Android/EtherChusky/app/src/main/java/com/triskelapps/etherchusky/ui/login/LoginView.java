package com.triskelapps.etherchusky.ui.login;

/**
 * Created by julio on 16/12/15.
 */
public interface LoginView {

    void configureForm(String type);

    void onEmailValidationError(String error);

    void onPasswordValidationError(String error);

    /**
     * Login or Sign up request success
     */
    void onSuccess();

    /**
     * Login or Sign up request error
     */
    void onError(String errorMessage);
}
