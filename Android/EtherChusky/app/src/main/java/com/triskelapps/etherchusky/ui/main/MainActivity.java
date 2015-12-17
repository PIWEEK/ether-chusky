package com.triskelapps.etherchusky.ui.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.triskelapps.etherchusky.BaseActivity;
import com.triskelapps.etherchusky.BasePresenter;
import com.triskelapps.etherchusky.R;
import com.triskelapps.etherchusky.model.Account;

import java.util.List;


public class MainActivity extends BaseActivity implements MainView {

    MainPresenter presenter;
    private TextView tvUsername;
    private TextView tvBalance;
    private ListView listTransactions;

    private void findViews() {
        tvUsername = (TextView)findViewById( R.id.tv_username );
        tvBalance = (TextView)findViewById( R.id.tv_balance );
        listTransactions = (ListView)findViewById( R.id.list_transactions );
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        presenter = MainPresenter.getInstance(this, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_payment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showTransactionDialog();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.onResume();
    }

    private void showTransactionDialog() {

        TransactionDialog.newInstance(presenter).show(getSupportFragmentManager(), null);


//        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
    }


    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setUsername(String username) {

        tvUsername.setText(username);
    }

    @Override
    public void setBalance(String balance) {

    }

    @Override
    public void listAccounts(List<Account> accounts) {

        int size = accounts.size();
        toast("Accounts size: " + size);
    }
}
