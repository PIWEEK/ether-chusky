package com.triskelapps.etherchusky.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.triskelapps.etherchusky.BaseActivity;
import com.triskelapps.etherchusky.BasePresenter;
import com.triskelapps.etherchusky.R;
import com.triskelapps.etherchusky.ui.main.MainActivity;


public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginView, TabLayout.OnTabSelectedListener {


    private EditText editUsername, editEmail;
    private EditText editPassword, editRepeatPassword;
    private Button btnLogin;
    private ProgressDialog progressBar;
    private LoginPresenter presenter;
    private TabLayout tabLayout;

    private void findViews() {
        editUsername = (EditText) findViewById(R.id.edit_username);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);
        editRepeatPassword = (EditText) findViewById(R.id.edit_repeat_pass);
        btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = LoginPresenter.getInstance(this, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.sign_up).setTag(LoginPresenter.TAB_SIGN_UP));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.login).setTag(LoginPresenter.TAB_LOGIN));

        tabLayout.setOnTabSelectedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_login:

                String username = editUsername.getText().toString().trim();
                String email = editEmail.getText().toString().trim();
                String pass = editPassword.getText().toString().trim();
                String repeatPass = editRepeatPassword.getText().toString().trim();

                presenter.performLoginOrSignup(username, email, pass, repeatPass);

                break;
        }
    }


    @Override
    public void configureForm(String type) {
        switch (type) {

            case LoginPresenter.TAB_LOGIN:

                editEmail.setVisibility(View.GONE);
                editRepeatPassword.setVisibility(View.GONE);
                btnLogin.setText(R.string.login);

                break;


            case LoginPresenter.TAB_SIGN_UP:

                editEmail.setVisibility(View.VISIBLE);
                editRepeatPassword.setVisibility(View.VISIBLE);
                btnLogin.setText(R.string.sign_up);

                break;
        }
    }


    // PRESENTER CALLBACKS

    @Override
    public void onEmailValidationError(String error) {
        editEmail.setError(error);
    }

    @Override
    public void onPasswordValidationError(String error) {
        editPassword.setError(error);

    }

    @Override
    public void onSuccess() {

        startActivity(new Intent(this, MainActivity.class));

    }

    @Override
    public void onError(String errorMessage) {

        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


    // TABS
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        presenter.changeFormType((String) tab.getTag());

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
