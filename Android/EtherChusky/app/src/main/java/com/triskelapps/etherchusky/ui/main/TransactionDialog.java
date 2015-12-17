package com.triskelapps.etherchusky.ui.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.triskelapps.etherchusky.R;
import com.triskelapps.etherchusky.model.Account;

import java.util.List;

/**
 * Created by julio on 17/12/15.
 */
public class TransactionDialog extends DialogFragment implements MainView, AdapterView.OnItemClickListener {


    private static final String ARG_PRESENTER = "arg_presenter";
    private MainPresenter presenter;
    private EditText editAmount;
    private EditText editDestination;
    private ListView listAccounts;

    public static TransactionDialog newInstance(MainPresenter presenter) {

        TransactionDialog fragmentDialog = new TransactionDialog();

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PRESENTER, presenter);
        fragmentDialog.setArguments(bundle);

        return fragmentDialog;

    }


    private void findViews(View layout) {
        editAmount = (EditText) layout.findViewById(R.id.edit_amount);
        editDestination = (EditText) layout.findViewById(R.id.edit_destination);
        listAccounts = (ListView) layout.findViewById(R.id.list_accounts);

        listAccounts.setOnItemClickListener(this);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = (MainPresenter) getArguments().get(ARG_PRESENTER);
        presenter.addView(this);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder ab = new AlertDialog.Builder(getContext())
                .setTitle(R.string.transaction)
                .setPositiveButton(R.string.pay, null)
                .setNegativeButton(R.string.cancel, null);


        View layout = View.inflate(getContext(), R.layout.dialog_transaction, null);

        findViews(layout);
        ab.setView(layout);

        final AlertDialog dialog = ab.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog2) {

                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (checkFields()) {

                            float amount = Float.parseFloat(editAmount.getText().toString().trim());
                            String destination = editDestination.getText().toString().trim();

                            presenter.sendTransaction(amount, destination);

                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        return dialog;

    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.getAccounts();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        presenter.removeView(this);
    }

    // This logic should be on Presenter (maybe another presenter for this dialog)
    private boolean checkFields() {

        try {
            Float.parseFloat(editAmount.getText().toString().trim());
            if (editDestination.getText().toString().isEmpty()) {
                editDestination.setError(getString(R.string.select_destination_user));
                return false;
            }
        } catch (NumberFormatException e) {
            editAmount.setError(getString(R.string.invalid_amount));
            return false;
        }

        return true;
    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public void setBalance(String balance) {

    }

    @Override
    public void listAccounts(List<Account> accounts) {

        ArrayAdapter<Account> adapter = new ArrayAdapter<Account>(getContext(),
                android.R.layout.simple_list_item_1, accounts);

        listAccounts.setAdapter(adapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String username = ((Account)parent.getItemAtPosition(position)).getName();
        editDestination.setText(username);
    }
}
