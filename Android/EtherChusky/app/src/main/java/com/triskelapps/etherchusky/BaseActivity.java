package com.triskelapps.etherchusky;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;


public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    protected final String TAG = this.getClass().getSimpleName();
    private Activity activity;
    private ProgressDialog progress;
    private String[] dataEntitiesSubscribed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().setBaseView(this);
    }


    public abstract BasePresenter getPresenter();

    // ------------ UI NOTIFICATIONS -----------

    @Override
    public void toast(int stringResId) {
        toast(getString(stringResId));
    }

    @Override
    public void toast(final String mensaje) {

        Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

    }

    @Override
    public void alert(final String title, final String message) {

        AlertDialog.Builder ab = new AlertDialog.Builder(getBaseContext());

        if (title != null) {
            ab.setTitle(title);
        }

        ab.setMessage(message);
        ab.setNegativeButton(R.string.back, null);
        ab.show();


    }

    @Override
    public void alert(final String message) {

        alert(getString(R.string.attention), message);
    }

    @Override
    public void showProgress(String message) {

        progress = new ProgressDialog(this);
        progress.setMessage(message);
        progress.show();
    }


    @Override
    public void hideProgress() {

        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }


    // ----CONFIGURATIONS --

    protected void configureSecondLevelActivity() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
