package com.triskelapps.etherchusky.ui.main;


import com.triskelapps.etherchusky.model.Account;

import java.util.List;

/**
 * Created by julio on 16/12/15.
 */
public interface MainView {

    void setUsername(String username);

    void setBalance(String balance);

    void listAccounts(List<Account> accounts);

}
