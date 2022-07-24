package com.example.movietrackerv2.ui.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.movietrackerv2.R;
import com.example.movietrackerv2.ui.viewmodel.ConfirmationInterface;

// creates a dialog which asks for confirmation about an action determined by the confirmationInterface
public class ConfirmationDialogFragment extends DialogFragment {

    private String title;
    private String message;
    private ConfirmationInterface confirmationInterface;

    public ConfirmationDialogFragment(String title, String message, ConfirmationInterface confirmationInterface) {
        this.title = title;
        this.message = message;
        this.confirmationInterface = confirmationInterface;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton(R.string.genres_alert_dialog_ok_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        confirmationInterface.executeConfirmableAction(); // user clicked okay, execute confirmable action
                    }
                })
                .setNegativeButton(R.string.genres_alert_dialog_cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {}
                });

        return builder.create();
    }
}
