package com.example.movietrackerv2.ui.view.dialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

// a dialog which displays the message given as a parameter
public class InformationDialog extends DialogFragment {

    private String title;
    private String message;

    public InformationDialog(String title, String message) {
        this.title = title;
        this.message = message;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton("OK", null);

        return builder.create();
    }
}
