package com.triskelapps.etherchusky.ui.login;

import android.content.Context;
import android.util.Patterns;

import com.triskelapps.etherchusky.BasePresenter;
import com.triskelapps.etherchusky.BuildConfig;
import com.triskelapps.etherchusky.R;

/**
 * Created by julio on 16/12/15.
 */
public class LoginPresenter extends BasePresenter {

    protected static final String TAB_LOGIN = "TAB_LOGIN";
    protected static final String TAB_SIGN_UP = "TAB_SIGN_UP";

    private static LoginPresenter sInstance;
    private final Context context;
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

        if (BuildConfig.DEBUG) {
            view.onSuccess();
            return;
        }


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

    private void performLogin(String username, String pass) {

        baseView.showProgress(context.getString(R.string.loading));

        // ---

        baseView.hideProgress();
        view.onError("Mal mal y mal");
    }

    private void performSignUp(String username, String email, String pass) {

        baseView.showProgress(context.getString(R.string.loading));

        // ---

        baseView.hideProgress();
        view.onSuccess();

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
