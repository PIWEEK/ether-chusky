package com.triskelapps.etherchusky.ui.login;

import android.content.Context;
import android.util.Patterns;

import com.triskelapps.etherchusky.App;
import com.triskelapps.etherchusky.BasePresenter;
import com.triskelapps.etherchusky.R;
import com.triskelapps.etherchusky.api.AccountApi;
import com.triskelapps.etherchusky.api.ApiClient;
import com.triskelapps.etherchusky.model.Account;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by julio on 16/12/15.
 */
public class LoginPresenter extends BasePresenter {

    protected static final String TAB_LOGIN = "TAB_LOGIN";
    protected static final String TAB_SIGN_UP = "TAB_SIGN_UP";

    private static LoginPresenter sInstance;
    LoginView view;
    private String currentFormType;


    public static LoginPresenter getInstance(LoginView view, Context context) {

        if (sInstance == null) {
            sInstance = new LoginPresenter(view, context);

        }
        return sInstance;
    }

    private LoginPresenter(LoginView view, Context context) {

        this.view = view;
        this.context = context;

    }

    protected void onResume() {

        changeFormType(isAlreadyLoggedIn() ? TAB_LOGIN : TAB_SIGN_UP);
    }

    protected void changeFormType(String type) {

        this.currentFormType = type;
        view.configureForm(type);

    }

    private boolean isAlreadyLoggedIn() {
        return false;
    }

    public void performLoginOrSignup(String username, String email, String pass, String repeatPass) {

//        if (BuildConfig.DEBUG) {
//            view.onSuccess();
//            return;
//        }


        switch (currentFormType) {

            case LoginPresenter.TAB_LOGIN:

                if (!validatePasswords(pass, null)) {
                    return;
                }

                performLogin(username, pass);
                break;


            case LoginPresenter.TAB_SIGN_UP:

                if (!validateEmail(email)) {
                    view.onEmailValidationError(context.getString(R.string.wrong_email));
                }

                if (!validatePasswords(pass, repeatPass)) {
                    return;
                }

                performSignUp(username, email, pass);

                break;
        }

    }

    private AccountApi getApi() {
        return ApiClient.getInstance(context).create(AccountApi.class);
    }

    private void performLogin(String username, String pass) {

        baseView.showProgress(context.getString(R.string.loading));

        Account account = new Account(username, pass);
        getApi().login(username, account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                        baseView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {

                        baseView.hideProgress();
                        view.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(Boolean ok) {

                        view.onSuccess();
                    }
                });


    }

    private void performSignUp(String username, String email, String pass) {

        baseView.showProgress(context.getString(R.string.loading));

        getPrefs().edit().putString(App.SHARED_USERNAME, username).apply();

        Account account = new Account(username, email, pass);
        getApi().signUp(account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Account>() {
                    @Override
                    public void onCompleted() {

                        baseView.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {

                        view.onError(e.getMessage());
                        baseView.hideProgress();
                        view.onSuccess();
                        baseView.toast("Error");
                    }

                    @Override
                    public void onNext(Account account) {

                        view.onSuccess();
                        baseView.toast("OK");
                    }
                });

    }

    private boolean validatePasswords(String pass, String repeatPass) {

        if (pass.length() < 6) {
            view.onPasswordValidationError(context.getString(R.string.min_length_pass));
            return false;
        }

        if (repeatPass != null) {
            if (!pass.equals(repeatPass)) {
                view.onPasswordValidationError(context.getString(R.string.pass_dont_match));
                return false;
            }
        }

        return true;

    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).find();
    }
}
