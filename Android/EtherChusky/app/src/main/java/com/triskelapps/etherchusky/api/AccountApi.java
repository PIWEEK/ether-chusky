package com.triskelapps.etherchusky.api;


import com.triskelapps.etherchusky.model.Account;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

public interface AccountApi {

    /**
     *
     app.post(PREFIX + '/accounts', api.accountsPost);
     app.get(PREFIX + '/accounts', api.accountsGet);
     app.get(PREFIX + '/accounts/:name', api.accountGet);
     app.get(PREFIX + '/my-account', api.myAccountGet);
     app.post(PREFIX + '/contracts', api.contractsPost);
     app.post(PREFIX + '/contracts/:address/:methodname', api.contractMethodPost);

     */


    // Add new account
    @POST("/web3-api/accounts")
    Observable<Account> signUp(@Body Account account);

    // Login. Return session coockie
    @POST("/web3-api/accounts/{name}/login")
    Observable<Boolean> login(@Path("name") String name, @Body Account account);

    // List accounts. Only visible parts (name)
    @GET("/web3-api/accounts")
    Observable<List<Account>> getAccounts();

}
