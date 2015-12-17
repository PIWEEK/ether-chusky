package com.triskelapps.etherchusky.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.triskelapps.etherchusky.BaseActivity;
import com.triskelapps.etherchusky.BasePresenter;
import com.triskelapps.etherchusky.R;


public class MainActivity extends BaseActivity implements MainView {

    MainPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MainPresenter.getInstance(this, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_payment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        presenter.pruebaToast("hoal");
    }


    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }
}
